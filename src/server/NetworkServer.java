package server;

import game.Direction;
import game.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NetworkServer {
	
	public static final Charset charset = StandardCharsets.UTF_8;
	public static final String protocolName =  "Schwartzenegger";
	public static final String protocolVersion = "1.0";
	public static final long minStateDelayMs = 50;
	public static final long maxStateDelayMs = 20000;
	private static final Pattern actionsRegex = Pattern.compile(protocolName + " " +
			"(\\d+(?:\\.\\d+)*) ((?:\\nMove(?:up|down|left|right))*)");
	private static final Pattern actionRegex = Pattern.compile("^move(up|down|left|right)$");
	private static final Pattern loginRegex = Pattern.compile(protocolName + " " +
			"(\\d+(?:\\.\\d+)*)\nLogin (.+)");
	

	
	DatagramSocket sock;
	GameServer gameServer;
	byte[] receiveBuffer;
	byte[] loginSendBuffer;
	byte[] sendStateBuffer;
	volatile boolean shutdownInitiated = false;
	
	private DatagramPacket receivePacket() throws IOException, SocketTimeoutException {
		DatagramPacket out = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		sock.receive(out);
		return null;
	}
	
	private void handlePacket(DatagramPacket packet) {
		byte[] bytes = packet.getData();
		String data = new String(bytes, 0,  bytes.length, charset);
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		Matcher actionMatch = actionsRegex.matcher(data); 
		if (actionMatch.matches()) {
			ServerPlayer player = gameServer.getPlayer(address, port);
			handleActionPacket(actionMatch.group(2), player);
			return;
		}
		
		Matcher loginMatch = loginRegex.matcher(data);
		if (loginMatch.matches()) {
			String playerName = loginMatch.group(2);
			String protocolVersion = loginMatch.group(1);
			handleLogin(playerName, protocolVersion, address, port);
		}
	}
	
	private void handleLogin(String playerName, String protocolVersion, InetAddress address, int port) {
		
		if (!NetworkServer.protocolVersion.equals(protocolVersion)) {
			//TODO fail: protocol mismatch
		}
		
		ServerPlayer player = gameServer.createPlayer(playerName, address, port);
		
		if (player == null) {
			//TODO fail: denied
		} else {
			//TODO success
		}
		
	}
	
	private void handleActionPacket(String actions, ServerPlayer player) {
		for (String action : actions.split("\n")) {
			Matcher match = actionRegex.matcher(action);
			String directionString = match.group(1);
			Direction direction = Direction.fromString(directionString);
			gameServer.movePlayer(player, direction);
		}
	}
	
	private void sendRawMessage(byte[] msg, int length, InetAddress address, int port) {
		DatagramPacket pack = new DatagramPacket(msg, length, address, port);
		try {
			sock.send(pack);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	private void sendStatePackets() {
		//TODO 
	}
	
	private void initiateShutdown() {
		//TODO 
	}
	
	private class SendStateThread implements Runnable {

		private long lastStateSent = 0;
		
		@Override
		public void run() {
			while (!shutdownInitiated) {
				long currentTime = System.currentTimeMillis();
				if (lastStateSent + minStateDelayMs < currentTime) {
					lastStateSent = currentTime;
					sendStatePackets();
				}
				try {
					synchronized (this) {
						wait(maxStateDelayMs);
					}
				} catch (InterruptedException e) {
					/*nothing*/
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
				} catch (SocketTimeoutException e) {
					/*Nothing, just keep looping*/
				} catch (Exception e) {
					e.printStackTrace();
					initiateShutdown();
				}
			}
		}
		
	}
}
