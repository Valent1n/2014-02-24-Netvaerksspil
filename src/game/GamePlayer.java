package game;

import java.util.HashSet;
import java.util.Set;

public class GamePlayer {

	// Players start values
	// private String playerDirection = "up";

	Player me;

	private String wall = "w";
	private KeyClass ko;
	private Set<Player> players;
	private Network network;

	ScoreList slist;

	// level is defined column by column
	private String[] level;
//	private String[][] level = {
//			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
//					"w", "w", "w", "w", "w", "w", "w" },
//			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "e",
//					"e", "e", "e", "e", "e", "e", "w" },
//			{ "w", "e", "w", "e", "e", "w", "e", "e", "w", "w", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "w", "e", "e", "w", "e", "e", "e", "w", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
//					"e", "e", "e", "e", "e", "e", "w" },
//			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "w", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "e", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "e", "w", "e", "w", "e", "e", "w", "e", "e", "w",
//					"e", "e", "w", "e", "e", "e", "w" },
//			{ "w", "e", "e", "e", "e", "e", "w", "e", "e", "w", "e", "e", "w",
//					"e", "e", "w", "e", "e", "e", "w" },
//			{ "w", "e", "w", "w", "e", "w", "w", "e", "e", "e", "e", "e", "e",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "w", "e", "w", "e", "e", "e", "e", "w", "e", "e",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "w",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "e", "e",
//					"e", "e", "w", "e", "e", "w", "w" },
//			{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
//					"e", "e", "e", "e", "e", "w", "w" },
//			{ "w", "e", "e", "w", "e", "w", "w", "w", "e", "e", "w", "e", "w",
//					"e", "e", "w", "w", "e", "w", "w" },
//			{ "w", "e", "w", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
//					"e", "e", "e", "e", "e", "w", "w" },
//			{ "w", "e", "e", "e", "w", "e", "e", "e", "w", "w", "e", "e", "w",
//					"e", "e", "e", "e", "e", "e", "w" },
//			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
//					"w", "w", "w", "w", "w", "w", "w" }, };
	// level is defined column by column
	Screen screen;

	public GamePlayer(Network network) {
		this.network = network;
		players = new HashSet<Player>();
	}
	
	public void startGame(String level, String name, int id){
		//TODO
		setMe(name, id);
		createMap(level);
//		ko = new KeyClass(this, me);
//		screen = new Screen(this.level, me.getXpos(), me.getYpos());
//		screen.setVisible(true);
//		screen.addKeyListener(ko);
	}
	
	public void createMap(String levelFromNetwork){
		//TODO
		String [] mapDimensonsAndMapRows = levelFromNetwork.split(" ");
		String [] mapRows = levelFromNetwork.split("\n");
		int yDim = Integer.parseInt(mapDimensonsAndMapRows[2]);
		String[] newLevel = new String[yDim];
		for(int i = 1; i < mapRows.length; i++){
			newLevel[i-1] = mapRows[i];
		}
		level = newLevel;
 	}
	
	
	public Player getMe() {
		return me;
	}

	public void setMe(String name, int id) {
		if(me == null){
			me = new Player(name, id);
			players.add(me);
		} else{
			if(players.contains(me)){
				players.remove(me);
			}
			me = new Player(name, id);
			players.add(me);
		}
		
	}

	public void moveMe(Direction direction){
		network.sendMove(direction);
	}
	
	public void shoot(){
		network.sendShoot();
	}
	
	public void logOff() {
		network.logOff();
	}

	public void PlayerMoved(Direction direction, Player player) {
		// TODO Spilleren med id fra parameter bevæges
		player.setDirection(direction);;
		int x = player.getXpos(), y = player.getYpos();
		if (direction.equals(Direction.RIGHT)) {
			x = player.getXpos() + 1;
		}
		;
		if (direction.equals(Direction.LEFT)) {
			x = player.getXpos() - 1;
		}
		;
		if (direction.equals(Direction.UP)) {
			y = player.getYpos() - 1;
		}
		;
		if (direction.equals(Direction.DOWN)) {
			y = player.getYpos() + 1;
		}
		;
		if (level[y].charAt(x) == 'w' ) {
			player.subOnePoint();
			slist.updateScoreOnScreen(player);
		} else {
			player.addOnePoint();
			slist.updateScoreOnScreen(player);
			screen.movePlayerOnScreen(player.getXpos(), player.getYpos(), x, y,
					player.getDirection());
			player.setXpos(x);
			player.setYpos(y);
		}
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Player isIdValid(int id) {
		for (Player p : this.getPlayers()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}

	// TODO Getplayerid, hvis id'et ikke findes i settet så oprettes spilleren,
	// ellers opdateres der
}
