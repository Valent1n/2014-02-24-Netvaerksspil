package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayer {

	// Players start values
	// private String playerDirection = "up";

	Player me;

	private char wall = 'w';
	private KeyClass ko;
	private List<Player> players;
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

	public GamePlayer(Player me, ScoreList s, String[] level, Network network) {
		this.network = network;
		players = new ArrayList<Player>();
		this.level = level;
		this.me = me;
		players.add(me);
		this.slist = s;
		screen = new Screen(level);
		screen.setVisible(true);
		ko = new KeyClass(this, me);
		screen.addKeyListener(ko);

	}

	public GamePlayer(Player me, Network network) {
		this.network = network;
		players = new ArrayList<Player>();
		this.me = me;
		players.add(me);
		this.slist = new ScoreList(players);
		slist.setVisible(true);
		screen = new Screen(level);
		screen.setVisible(true);
		ko = new KeyClass(this, me);
		screen.addKeyListener(ko);
	}

	public GamePlayer() {
		players = new ArrayList<Player>();
	}
	
	public void startGame(String level, String name, int id){
		//TODO
		setMe(name, id);
		createMap(level);
		ko = new KeyClass(this, me);
		screen = new Screen(this.level);
		screen.addPlayer(me.getXpos(), me.getYpos(), me.getDirection());
		screen.setVisible(true);
		screen.addKeyListener(ko);
		this.slist = new ScoreList(players);
		slist.setVisible(true);
	}
	
	public void createMap(String levelFromNetwork){
		//TODO
		String [] mapRows = levelFromNetwork.split("\n");
		String [] mapDimensonsAndMapRows =mapRows[0].split(" ");
		int yDim = Integer.parseInt(mapDimensonsAndMapRows[2]);
		String[] newLevel = new String[yDim];
		for(int i = 1; i < mapRows.length; i++){
			newLevel[i-1] = mapRows[i];
		}
		level = newLevel;
 	}
	
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Player getMe() {
		return me;
	}

	public void setMe(String name, int id) {
		if(me == null){
			me = new Player(name, id, 0, 0);
			players.add(me);
		} else{
			if(players.contains(me)){
				players.remove(me);
			}
			me = new Player(name, id, 0, 0);
			players.add(me);
		}
		
	}

	public void moveMe(Direction direction){
		network.sendMove(direction);
		// TODO Spilleren med id fra parameter bevæges
				me.setDirection(direction);
				int x = me.getXpos(), y = me.getYpos();
				if (direction.equals(Direction.RIGHT)) {
					x = me.getXpos() + 1;
				}
				;
				if (direction.equals(Direction.LEFT)) {
					x = me.getXpos() - 1;
				}
				;
				if (direction.equals(Direction.UP)) {
					y = me.getYpos() - 1;
				}
				;
				if (direction.equals(Direction.DOWN)) {
					y = me.getYpos() + 1;
				}
				;
				if (level[y].charAt(x) == wall) {
					me.subOnePoint();
				} else {
					me.addOnePoint();
					screen.movePlayerOnScreen(me.getXpos(), me.getYpos(), x, y,
							me.getDirection());
					me.setXpos(x);
					me.setYpos(y);
				}
	}
	
	public void shoot(){
		network.sendShoot();
	}
	
	public void logOff() {
		network.logOff();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
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
		slist.players = players;
		slist.updateScoreOnScreen(p);
	}

	// TODO Getplayerid, hvis id'et ikke findes i settet så oprettes spilleren,
	// ellers opdateres der
}
