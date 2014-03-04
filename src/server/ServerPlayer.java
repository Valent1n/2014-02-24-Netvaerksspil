package server;

import java.net.InetAddress;

import game.Player;

public class ServerPlayer extends Player {

	
	private InetAddress ip;
	private int port;
	private long lastLifeSign;
	public ServerPlayer(String name, int id, InetAddress ip, int port) {
		super(name, id);
		this.ip = ip;
		this.port = port;
	}
	
	
	
}
