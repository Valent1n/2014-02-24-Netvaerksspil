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
		System.out.println("Indtast Servernavn:");
		String serverName = b.readLine();
		 
		players = new ArrayList<Player>();
		me = new Player(username, -1);
		players.add(me);
		
//		Network network = new Network(username, null);
//		GamePlayer g = new GamePlayer(me, network);
//		network.setGamePlayer(g);
		
//		ScoreList s = new ScoreList(players);
//		s.setVisible(true);
		GamePlayer g = new GamePlayer();
		Network network = new Network(username, g);
		g.setNetwork(network);
	}

}
