package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	private static Network singleton;
	private GamePlayer gamePlayer;
	private DatagramSocket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private static final Charset charset = StandardCharsets.UTF_8;
	private static final String protocolName = "Schwarzenegger";
	private static final String protocolVersion = "1.0";
	private static final int bufferSize = 65536;
	private static final String host = "localhost";
	private static final int port = 7542;
	private int id;
	private InputThread it;
	private OutputThread ot;

	private Network() {
		singleton = new Network();
		try {
			// 1. Opret en socket
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Network getNetwork() {
		if (singleton != null)
			singleton = new Network();
		return singleton;
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

	public boolean logon(String host, int port, String brugernavn) {
		String msg = protocolName + " " + protocolVersion + "\n" + "Login"
				+ " " + brugernavn;
		System.out.println("Prøver at forbinde til " + socket.getInetAddress()
				+ " port: " + socket.getPort());
		try {
			sendPacket(host, port, msg);
			String received = null;
			while (received == null) {
				received = receivePacket(host, port);
			}
			System.out.println(received);
			String[] stringArrayProtocol = received.split("\n");
			String[] stringArrayContent = stringArrayProtocol[1].split(" ");
			if (checkProtocol(stringArrayProtocol[0])) {
				if (stringArrayContent[0].equals("Granted")) {
					System.out.println("Login granted!");
					id = Integer.parseInt(stringArrayContent[1]);
					return true;
				} else {
					System.out.println("Login denied!");
				}

			} else {
				System.out.println("Protocol mismatch.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Kunne ikke forbinde til "
					+ socket.getInetAddress() + " port: " + socket.getPort());
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
			String brugernavn = "Lars";
			if (logon(host, port, brugernavn)) {
				System.out.println("Forbundet til " + socket.getInetAddress()
						+ " port: " + socket.getPort());
				gamePlayer = new GamePlayer(new Player(brugernavn, id), this);
				// 2: Start trådene som snakker med serveren
				it = new InputThread();
				ot = new OutputThread();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
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

		public void sendAction(){
			String msg = protocolName + " " + protocolVersion;
			for(String action : actionBuffer){
			msg += action;
			}
			actionBuffer.clear();
			try {
				sendPacket(host, port, msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Kunne ikke forbinde til "
						+ socket.getInetAddress() + " port: " + socket.getPort());
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
						// TODO Auto-generated catch block
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
				p.setDirection(playerUpdate[5]);
			} else{
				// Opret ny spiller
				p = new Player(playerUpdate[4], Integer.parseInt(playerUpdate[0]));
				p.setXpos(Integer.parseInt(playerUpdate[1]));
				p.setYpos(Integer.parseInt(playerUpdate[2]));
				p.setPoint(Integer.parseInt(playerUpdate[3]));
				p.setDirection(playerUpdate[5]);
				gamePlayer.addPlayer(p);
			}
			i++;
		}
	}
	}
}
