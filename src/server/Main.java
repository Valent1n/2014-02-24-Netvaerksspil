package server;

import java.io.IOException;
import java.net.SocketException;

public class Main {

	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
		NetworkServer networkServer = null;
		try {
			
			networkServer = new NetworkServer(gameServer, 7542);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		System.out.println("Press enter to kill server");
		try {
			System.in.read();	//TODO make more better
			networkServer.close();
			gameServer.close();
			System.out.println("Server closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
