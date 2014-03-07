package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayer {

	Player me;

	private char wall = 'w';
	private KeyClass keyController;
	private List<Player> players;
	private Network network;

	ScoreList slist;

	private String[] level;
	Screen screen;


	public GamePlayer() {
		players = new ArrayList<Player>();
	}
	
	public void startGame(String level, Player me){
		//TODO
		setMe(me);
		createMap(level);
		keyController = new KeyClass(this, me);
		screen = new Screen(this.level);
		screen.addPlayer(me);
		screen.setVisible(true);
		screen.addKeyListener(keyController);
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

	public void setMe(Player me) {
		this.me = me;
		if (!players.contains(me)) {
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
		screen.addPlayer(p);
		p.addObserver(screen);
		slist.players = players;
//		slist.updateScoreOnScreen(p);
	}

	// TODO Getplayerid, hvis id'et ikke findes i settet så oprettes spilleren,
	// ellers opdateres der
}