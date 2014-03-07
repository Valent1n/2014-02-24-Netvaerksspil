package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Game {

	/**
	 * @param args
	 *
	 */
	public static ArrayList<Player> players;
	public static Player me;
	
	public static void main(String[] args) throws Exception {
	
		System.out.println("Indtast dit spillernavn");
		BufferedReader b = new BufferedReader (new InputStreamReader(System.in));
		String username = b.readLine();
		GamePlayer g = new GamePlayer();
		Network network = new Network(username, g);
		g.setNetwork(network);
	}

}
