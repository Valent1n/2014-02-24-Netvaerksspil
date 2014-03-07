package server;

import game.Direction;
import game.Player;
import game.PlayerObserver;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkServer implements PlayerObserver, Closeable {

	public static final Charset charset = StandardCharsets.UTF_8;
	public static final String protocolName = "Schwarzenegger";
	public static final String protocolVersion = "1.0";
	public static final long minStateDelayMs = 50;
	public static final long maxStateDelayMs = 2000;

	private static final int socketTimeout = 2000;
	private static final String versionRegex = " (\\d+(?:\\.\\d+)*)\n";
	private static final String greetingRegex = protocolName + versionRegex;
	private static final Pattern actionsPatt = Pattern.compile(greetingRegex
			+ "((?:move(?:up|down|left|right)\n?)*)");
	private static final Pattern movePatt = Pattern
			.compile("^move(up|down|left|right)$");
	private static final Pattern shootPatt = Pattern.compile("shoot");
	private static final Pattern loginPatt = Pattern.compile(greetingRegex
			+ "Login ([\\w]+)");
	private static final Pattern logoffPatt = Pattern.compile(greetingRegex
			+ "Logoff");

	private static final byte[] loginDenied = (protocolName + " "
			+ protocolVersion + "\nDenied").getBytes(charset);

	private DatagramSocket sock;
	private GameServer gameServer;
	private byte[] receiveBuffer = new byte[1 << 16]; // 65536, same as 2^16
	private volatile boolean shutdownInitiated = false;
	private Thread receiveThread;
	private Thread sendStateThread;

	public NetworkServer(GameServer gameServer, int udpPort)
			throws SocketException {
		sock = new DatagramSocket(udpPort);
		sock.setSoTimeout(socketTimeout);
		this.gameServer = gameServer;
		sendStateThread = new Thread(new SendStateThread());
		receiveThread = new Thread(new ReceiveThread());
		sendStateThread.start();
		receiveThread.start();

	}

	private DatagramPacket receivePacket() throws IOException,
			SocketTimeoutException {
		DatagramPacket out = new DatagramPacket(receiveBuffer,
				receiveBuffer.length);
		sock.receive(out);
		return out;
	}

	private void handlePacket(DatagramPacket packet) {
		byte[] bytes = packet.getData();
		String data = new String(bytes, 0, packet.getLength(), charset);
		InetAddress address = packet.getAddress();
		int port = packet.getPort();

		Matcher actionMatch = actionsPatt.matcher(data);
		if (actionMatch.matches()) {
			ServerPlayer player = gameServer.getPlayer(address, port);
			handleActionPacket(actionMatch.group(2), player);
			return;
		}
		Matcher loginMatch = loginPatt.matcher(data);
		if (loginMatch.matches()) {
			String playerName = loginMatch.group(2);
			String protocolVersion = loginMatch.group(1);
			handleLogin(playerName, protocolVersion, address, port);
			return;
		}

		Matcher logoffMatch = logoffPatt.matcher(data);
		if (logoffMatch.matches()) {
			handleLogoff(address, port);
			return;
		}
	}

	private void handleLogin(String playerName, String protocolVersion,
			InetAddress address, int port) {

		if (!NetworkServer.protocolVersion.equals(protocolVersion)) {
			sendRawMessage(loginDenied, loginDenied.length, address, port);
		}

		ServerPlayer player = gameServer
				.createPlayer(playerName, address, port);

		if (player == null) {
			sendRawMessage(loginDenied, loginDenied.length, address, port);
		} else {
			// success
			byte[] bytes = createLoginGrantedMessage(player);
			player.addObserver(this);
			sendRawMessage(bytes, bytes.length, address, port);
		}
		sendStateThread.interrupt();

	}

	private byte[] createLoginGrantedMessage(ServerPlayer player) {
		StringBuilder sb = new StringBuilder();
		sb.append(NetworkServer.protocolName + " "
				+ NetworkServer.protocolVersion + "\nGranted ");
		sb.append(player.getId());
		sb.append("\n");
		String[] mapLayout = gameServer.getMapLayout();
		sb.append("Map ");
		sb.append(mapLayout[0].length());
		sb.append(" ");
		sb.append(mapLayout.length);
		for (String row : mapLayout) {
			sb.append("\n");
			sb.append(row);
		}
		byte[] bytes = sb.toString().getBytes(charset);
		return bytes;
	}

	private void handleActionPacket(String actions, ServerPlayer player) {
		player.registerLifeSign();
		for (String action : actions.split("\n")) {
			Matcher moveMatch = movePatt.matcher(action);
			if (moveMatch.matches()) {
				String directionString = moveMatch.group(1);
				Direction direction = Direction.fromString(directionString);
				gameServer.movePlayer(player, direction);
				continue;
			}
			Matcher shootMatch = shootPatt.matcher(action);
			if (shootMatch.matches()) {
				gameServer.shoot(player);
			}
		}
	}

	private void sendRawMessage(byte[] msg, int length, InetAddress address,
			int port) {
		DatagramPacket pack = new DatagramPacket(msg, length, address, port);
		try {
			sock.send(pack);
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void handleLogoff(InetAddress address, int port) {
		gameServer.removePlayer(address, port);
	}

	private void sendStatePackets() {
		StringBuilder sb = new StringBuilder();
		Set<ServerPlayer> players = gameServer.getPlayers();
		sb.append(protocolName + " " + protocolVersion);

		String pattern = "\n%d %d %d %d %s %d";
		for (ServerPlayer p : players) {
			String line = String.format(pattern, p.getId(), p.getXpos(), p
					.getYpos(), p.getPoint(), p.getName(), p.getDirection()
					.ordinal());
			sb.append(line);
			// String hej = "\n" + p.getId() + " " + p.getXpos() + " " +
			// p.getYpos() + " " + p.getPoint() + " " + p.getName() + " " +
			// p.getDirection().ordinal();
			// sb.append(hej);
		}
		byte[] bytes = sb.toString().getBytes(charset);

		for (ServerPlayer p : players) {
			sendRawMessage(bytes, bytes.length, p.getIp(), p.getPort());
		}

	}

	@Override
	public void update(Player player) {
		sendStateThread.interrupt();

	}

	private void initiateShutdown() {
		if (!shutdownInitiated) {
			shutdownInitiated = true;
			sendStateThread.interrupt();
			receiveThread.interrupt();
		}
	}

	public boolean isShutdownInitiated() {
		return shutdownInitiated;
	}

	public boolean isAlive() {
		return receiveThread.isAlive() || sendStateThread.isAlive();
	}

	@Override
	public void close() throws IOException {
		initiateShutdown();
		while (receiveThread.isAlive() || sendStateThread.isAlive()) {
			try {
				receiveThread.join(0);
				sendStateThread.join(0);
			} catch (InterruptedException e) {
				// nothing, just loop
			}
		}
	}

	private class SendStateThread implements Runnable {

		@Override
		public void run() {
			long lastStateSent = 0;
			long waitPeriod;

			while (!shutdownInitiated) {
				long currentTime = System.currentTimeMillis();
				if (lastStateSent + minStateDelayMs <= currentTime) {
					lastStateSent = currentTime;
					sendStatePackets();
					waitPeriod = maxStateDelayMs;
				} else {
					waitPeriod = currentTime - lastStateSent + minStateDelayMs;
				}

				try {
					synchronized (this) {
						wait(waitPeriod);
					}
				} catch (InterruptedException e) {
					/* nothing */
				}
			}
		}

	}

	private class ReceiveThread implements Runnable {

		@Override
		public void run() {
			while (!shutdownInitiated) {
				try {
					DatagramPacket packet = receivePacket();
					handlePacket(packet);
					byte[] byteArray = packet.getData();
					String reconstitutedString = new String(byteArray);
					System.out.println(reconstitutedString);
				} catch (SocketTimeoutException e) {
					/* Nothing, just keep looping */
				} catch (Exception e) {
					e.printStackTrace();
					initiateShutdown();
				}
			}
		}

	}

}
