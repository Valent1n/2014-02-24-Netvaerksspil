package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Game {

	public static void main(String[] args) throws Exception {
	
		GamePlayer g  = new GamePlayer();
		Network network;
		System.out.print("Indtast dit spillernavn: ");
		BufferedReader b = new BufferedReader (new InputStreamReader(System.in));
		String username = b.readLine();

		System.out.println("Indtast Servernavn, eller enter for localhost:");
		String serverName = b.readLine();
		
		if (serverName.equals("")) {
			network = new Network(username, g);
		} else {
			Integer serverPort = null;
			while (serverPort == null) {
				System.out.print("Indtast portnummer: ");
				String portString = b.readLine();
				try {
					serverPort = Integer.parseInt(portString);
				} catch (Exception e) {/*Do nothing, we'll loop*/}
			}
			network = new Network(username, serverName, serverPort, g);
		}

		g.setNetwork(network);
	}

}
