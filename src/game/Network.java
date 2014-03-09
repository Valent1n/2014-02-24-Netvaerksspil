package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Network extends Thread {

	public static final int defaultPort = 7542;
	public static final String defaultHost = "localhost";
	public static final String protocolName = "Schwarzenegger";
	public static final String protocolVersion = "1.0";
	private static final Charset charset = StandardCharsets.UTF_8;
	private static final int bufferSize = 65536;
	
	private GamePlayer gamePlayer;
	private DatagramSocket socket;
	private String host;
	private int port;
	private String name;
	@SuppressWarnings("unused")
	private InputThread it;
	private OutputThread ot;
	private static final long minActionDelayMs = 50;
	private static final long maxActionDelayMs = 5000;
	private int tempStartX = 4, tempStartY = 5;

	
	//  Temp constructor - bruger faste værdier af ip og port
	public Network(String name, GamePlayer gamePlayer) {
		this(name, defaultHost, defaultPort, gamePlayer);
	}

	
	public Network(String name, String host, int port, GamePlayer gamePlayer) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.gamePlayer = gamePlayer;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.start();
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public boolean checkProtocol(String protocol) {
		String protocol2 = protocolName + " " + protocolVersion;
		return protocol2.equals(protocol);
	}
	
	public void sendShoot() {
		ot.sendShoot();
	}
	
	public void sendMove(Direction direction) {
		ot.sendMove(direction);
	}
	
	public void logOff() {
		ot.logOff();
	}

	public boolean logon() {
		String msg = protocolName + " " + protocolVersion + "\n" + "Login"
				+ " " + name;
		try {
			sendPacket(host, port, msg);
			String received = null;
			while (received == null) {
				received = receivePacket(host, port);
			}
			//Adskil greeting, login og userid, map
			String[] stringArrayProtocol = received.split("\n", 3);
			//Adskil login og userid
			String[] stringArrayContent = stringArrayProtocol[1].split(" ");
			if (checkProtocol(stringArrayProtocol[0])) {
				if (stringArrayContent[0].equals("Granted")) {
					int id = Integer.parseInt(stringArrayContent[1]);
					String level = stringArrayProtocol[2];
					Player player = new Player(name, id, tempStartX, tempStartY);
					gamePlayer.startGame(level, player);
					return true;
				} else {
					//TODO Error - login denied
				}

			} else {
				//TODO throw error - protocol mismatch
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void sendPacket(String host, int port, String msg)
			throws IOException {
		byte[] bytes = msg.getBytes(charset);
		InetAddress ipAddress = InetAddress.getByName(host);
		DatagramPacket p = new DatagramPacket(bytes, bytes.length, ipAddress,
				port);
		socket.send(p);
	}

	public String receivePacket(String host, int port) throws IOException {
		byte[] buffer = new byte[bufferSize];
		DatagramPacket receivedPacket = new DatagramPacket(buffer,
				buffer.length);
		socket.receive(receivedPacket);
		String receive = new String(receivedPacket.getData(), 0,
				receivedPacket.getLength(), charset);
		return receive;
	}

	public void run() {

		try {
			// 1. Prøv at forbinde til hosten
			if (logon()) {
				// 2: Start trådene som snakker med serveren
				it = new InputThread();
				ot = new OutputThread();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			//TODO make better
		}
	}

	class OutputThread extends Thread {
		
		private List<String> actionBuffer = new LinkedList<>();

		public OutputThread() throws IOException {
			this.start();
		}

		public void run() {
			long lastActionPacketSent = 0;
			long waitPeriod;
			while (true) {
				long currentTime = System.currentTimeMillis();
				long msUntilPacketCanBeSent = lastActionPacketSent + minActionDelayMs - currentTime;
				
				if (msUntilPacketCanBeSent <= 0) {
					lastActionPacketSent = currentTime;
					sendAction();
					waitPeriod = maxActionDelayMs;
					
				} else {
					waitPeriod = msUntilPacketCanBeSent;
				}
				
				
				try {
					synchronized (this) {
						wait(waitPeriod);
					}
				} catch (InterruptedException e) {
					/*nothing*/
				}
			}
		}
		
		public void logOff() {
			String msg = protocolName + " " + protocolVersion + "\nLogoff";
			try {
				sendPacket(host, port, msg);
			} catch (IOException e) {
				//Do nothing
			}
			System.exit(0);
		}

		public void sendAction(){
			String msg = protocolName + " " + protocolVersion;
			synchronized (actionBuffer) {
				for (String action : actionBuffer) {
					msg += action;
				}
				if (actionBuffer.size() == 0) {
					msg += "\n";
				}
				actionBuffer.clear();
			}
			try {
				sendPacket(host, port, msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void sendMove(Direction direction){
			String msg = "\nmove" + direction.toString().toLowerCase();
			synchronized (actionBuffer) {
				actionBuffer.add(msg);
			}
			ot.interrupt();
		}
		
		public void sendShoot(){
			String msg = "\n" + "shoot";
			synchronized (actionBuffer) {
				actionBuffer.add(msg);
			}
		}
		
		}
		
	


	class InputThread extends Thread {

		public InputThread() throws IOException {
			this.start();
		}

		public void run() {
			while (true) {
					String state;
					try {
						state = receivePacket(host, port);
						String[] stringArrayProtocol = state.split("\n", 2);
						if (checkProtocol(stringArrayProtocol[0])) {
							this.updateState(stringArrayProtocol[1]);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
			}		
		}
	
	public void updateState(String content) {
		String[] playerUpdate;
		String [] playerRows = content.split("\n");
		for(String playerRow : playerRows) {
			playerUpdate = playerRow.split(" ");
			
			int id = Integer.parseInt(playerUpdate[0]);
			int xPos = Integer.parseInt(playerUpdate[1]);
			int yPos = Integer.parseInt(playerUpdate[2]);
			int score = Integer.parseInt(playerUpdate[3]);
			String name = playerUpdate[4];
			int directionNumber = Integer.parseInt(playerUpdate[5]);
			Direction direction = Direction.values()[directionNumber]; 
			
			
			Player p = gamePlayer.getPlayer(id);
			if(p != null) {
				//Opdater spillerens variabler
				p.setXpos(xPos); 
				p.setYpos(yPos); 
				p.setPoint(score);
				p.setDirection(direction);
			} 
			else{
				// Opret ny spiller
				p = new Player(name, id, xPos, yPos, score, direction);
				gamePlayer.addPlayer(p);
			}
		}
	}
	}//end InputThread
}
