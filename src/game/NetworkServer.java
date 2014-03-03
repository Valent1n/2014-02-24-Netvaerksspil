package game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NetworkServer {
	
	static final Charset charset = StandardCharsets.UTF_8;
	static final String protocolName =  "Schwartzeneger";
	static final String protocolVersion = "1.0";
	private static final Pattern actionsRegex = Pattern.compile(protocolName + " " +
			"(\\d+(?:\\.\\d+)*) ((?:\\nMove(?:up|down|left|right))*)");
	private static final Pattern actionRegex = Pattern.compile("^move(up|down|left|right)$");
	private static final Pattern loginRegex = Pattern.compile(protocolName + " " +
			"(\\d+(?:\\.\\d+)*)\nLogin (.+)");

	
	DatagramSocket sock;
	GameServer gameServer;
	byte[] receiveBuffer;
	byte[] loginSendBuffer;
	
	
	
	private void parsePacket(DatagramPacket packet) {
		byte[] bytes = packet.getData();
		String data = new String(bytes, 0,  bytes.length, charset);
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		Matcher actionMatch = actionsRegex.matcher(data); 
		if (actionMatch.matches()) {
			Player player = gameServer.getPlayer(address, port);
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
		
		if (!this.protocolVersion.equals(protocolVersion)) {
			//TODO fail: protocol mismatch
		}
		
		Player player = gameServer.createPlayer(playerName, address, port);
		
		if (player == null) {
			//TODO fail: denied
		} else {
			//TODO success
		}
		
	}
	
	private void handleActionPacket(String actions, Player player) {
		for (String action : actions.split("\n")) {
			Matcher match = actionRegex.matcher(action);
			String directionString = match.group(1);
			Direction direction = Direction.fromString(directionString);
			movePlayer(player, direction);
		}
	}
	
	private void movePlayer(Player player, Direction direction) {
		//TODO 
	}
	
	private void sendRawMessage(byte[] msg, InetAddress address, int port) {
		//TODO 
		
	}
}
