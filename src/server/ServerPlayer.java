package server;

import java.net.InetAddress;

import game.Player;

public class ServerPlayer extends Player {

	
	private InetAddress ip;
	private int port;
	private long lastLifeSign;
	public ServerPlayer(String name, int id, int spawnX, int spawnY, InetAddress ip, int port) {
		super(name, id, spawnX, spawnY);
		this.ip = ip;
		this.port = port;
		registerLifeSign();
	}
	
	

	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getLastLifeSign() {
		return lastLifeSign;
	}
	public void registerLifeSign() {
		lastLifeSign = System.currentTimeMillis();
	}
	
	
	
}
