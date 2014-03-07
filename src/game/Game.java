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
		
//		 String[][] level = {
//					{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
//							"w", "w", "w", "w", "w", "w", "w", "w" },
//					{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "e",
//							"e", "e", "e", "e", "e", "e", "w" },
//					{ "w", "e", "w", "e", "e", "w", "e", "e", "w", "w", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "w", "e", "e", "w", "e", "e", "e", "w", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
//							"e", "e", "e", "e", "e", "e", "w" },
//					{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "w", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "e", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "e", "w", "e", "w", "e", "e", "w", "e", "e", "w",
//							"e", "e", "w", "e", "e", "e", "w" },
//					{ "w", "e", "e", "e", "e", "e", "w", "e", "e", "w", "e", "e", "w",
//							"e", "e", "w", "e", "e", "e", "w" },
//					{ "w", "e", "w", "w", "e", "w", "w", "e", "e", "e", "e", "e", "e",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "w", "e", "w", "e", "e", "e", "e", "w", "e", "e",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "w",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "e", "e",
//							"e", "e", "w", "e", "e", "w", "w" },
//					{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
//							"e", "e", "e", "e", "e", "w", "w" },
//					{ "w", "e", "e", "w", "e", "w", "w", "w", "e", "e", "w", "e", "w",
//							"e", "e", "w", "w", "e", "w", "w" },
//					{ "w", "e", "w", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
//							"e", "e", "e", "e", "e", "w", "w" },
//					{ "w", "e", "e", "e", "w", "e", "e", "e", "w", "w", "e", "e", "w",
//							"e", "e", "e", "e", "e", "e", "w" },
//					{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
//							"w", "w", "w", "w", "w", "w", "w", "w" }, };
		 
		players = new ArrayList<Player>();
		me = new Player(username, -1);
		players.add(me);
		players.add(new Player("FUP", -2));
		
		ScoreList s = new ScoreList(players);
		s.setVisible(true);
		
		GamePlayer g = new GamePlayer();
		g.setNetwork(new Network(username, g));
	}

}
