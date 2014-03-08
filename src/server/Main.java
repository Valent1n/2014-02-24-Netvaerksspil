package server;

import java.io.IOException;
import java.net.SocketException;

public class Main {

	public static void main(String[] args) throws SocketException {
		GameServer gameServer = new GameServer();
		NetworkServer networkServer = null;
		networkServer = new NetworkServer(gameServer, 7542);
		
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
