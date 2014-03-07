package game;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Network extends Thread {

	private GamePlayer gamePlayer;
	private DatagramSocket socket;
	private static final Charset charset = StandardCharsets.UTF_8;
	private static final String protocolName = "Schwarzenegger";
	private static final String protocolVersion = "1.0";
	private static final int bufferSize = 65536;
	private String host = "localhost";
	private int port = 7542;
	private String name;
	private InputThread it;
	private OutputThread ot;
	
	//  Temp constructor - bruger faste værdier af ip og port
	public Network(String name, GamePlayer gamePlayer) {
		this.name = name;
		this.gamePlayer = gamePlayer;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//TODO //LAV NETWORK OM TIL EN TRÅD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		run();
	}

	
	public Network(String name, String ip, int port, GamePlayer gamePlayer) {
		this.name = name;
		this.host = ip;
		this.port = port;
		this.gamePlayer = gamePlayer;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		//TODO //LAV NETWORK OM TIL EN TRÅD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		run();
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
		System.out.println("Prøver at forbinde til " + host
				+ " port: " + port);
		try {
			sendPacket(host, port, msg);
			String received = null;
			while (received == null) {
				received = receivePacket(host, port);
			}
			System.out.println(received);
			//Adskil greeting, login og userid, map
			String[] stringArrayProtocol = received.split("\n", 3);
			//Adskil login og userid
			String[] stringArrayContent = stringArrayProtocol[1].split(" ");
			if (checkProtocol(stringArrayProtocol[0])) {
				if (stringArrayContent[0].equals("Granted")) {
					System.out.println("Login granted!");
					int id = Integer.parseInt(stringArrayContent[1]);
					String level = stringArrayProtocol[2];
					gamePlayer.startGame(level, name ,id);
					return true;
				} else {
					System.out.println("Login denied!");
				}

			} else {
				System.out.println("Protocol mismatch.");
			}
		} catch (IOException e) {
			System.out.println("Kunne ikke forbinde til "
					+ host + " port: " + port);
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
				System.out.println("Forbundet til " + host
						+ " port: " + port);
				// 2: Start trådene som snakker med serveren
				it = new InputThread();
				ot = new OutputThread();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
		}
	}

	class OutputThread extends Thread {
		
		private List<String> actionBuffer = new LinkedList<String>();

		public OutputThread() throws IOException {
			this.start();
		}

		public void run() {
			while (true) {
				
			}
		}
		
		public void logOff() {
			String msg = protocolName + " " + protocolVersion + " Logoff";
			try {
				sendPacket(host, port, msg);
			} catch (IOException e) {
				System.out.println("Logoff failed!" + e.getMessage());
			}
			System.out.println(name + " has successfully been logged out!");
			System.exit(0);
			// TODO: Kill application
		}

		public void sendAction(){
			String msg = protocolName + " " + protocolVersion;
			for(String action : actionBuffer){
			msg += action;
			}
			actionBuffer.clear();
			try {
				sendPacket(host, port, msg);
			} catch (IOException e) {
				System.out.println("Kunne ikke forbinde til "
						+ host + " port: " + port);
				e.printStackTrace();
			}
		}
		
		public void sendMove(Direction direction){
			String msg = "\n" + direction;
			actionBuffer.add(msg);
		}
		
		public void sendShoot(){
			String msg = "\n" + "Shoot";
			actionBuffer.add(msg);
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
						System.out.println("Server: " + state);
						String[] stringArrayProtocol = state.split("\n");
						if (checkProtocol(stringArrayProtocol[0])) {
							String[] content = Arrays.copyOfRange(stringArrayProtocol, 1, stringArrayProtocol.length);
							this.updateState(content);
						} else {
								System.out.println("Protocol mismatch!");
							}
					} catch (IOException e) {
						e.printStackTrace();
					}
			}		
		}
	
	public void updateState(String[] content) {
		String[] playerUpdate;
		for(int i = 0; i < content.length; i++) {
			playerUpdate = content[i].split(" ");
			Player p = gamePlayer.isIdValid(Integer.parseInt(playerUpdate[0]));
			if(p != null) {
				//Opdater spillerens variabler
				p.setXpos(Integer.parseInt(playerUpdate[1]));
				p.setYpos(Integer.parseInt(playerUpdate[2]));
				p.setPoint(Integer.parseInt(playerUpdate[3]));
				p.setDirection(Direction.fromString(playerUpdate[5]));
			} else{
				// Opret ny spiller
				p = new Player(playerUpdate[4], Integer.parseInt(playerUpdate[0]));
				p.setXpos(Integer.parseInt(playerUpdate[1]));
				p.setYpos(Integer.parseInt(playerUpdate[2]));
				p.setPoint(Integer.parseInt(playerUpdate[3]));
				p.setDirection(Direction.fromString(playerUpdate[5]));
				gamePlayer.addPlayer(p);
			}
			i++;
		}
	}
	}
}
