package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Game {

	/**
	 * @param args
	 *
	 */
	private static ArrayList<Player> players;
	private static Player me;
	
	public static void main(String[] args) throws Exception {
	
		System.out.println("Indtast dit spillernavn");
		BufferedReader b = new BufferedReader (new InputStreamReader(System.in));
		String username = b.readLine();

//		System.out.println("Indtast Servernavn:");
//		String serverName = b.readLine();

//		players = new ArrayList<Player>();
//		me = new Player(username, -1);
//		players.add(me);

		GamePlayer g = new GamePlayer();
		Network network = new Network(username, g);
		g.setNetwork(network);
	}

}
