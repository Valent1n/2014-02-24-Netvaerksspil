package game;

import java.util.ArrayList;
import java.util.List;

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
		players = new ArrayList<>();
	}
	
	public void startGame(String level, Player me){
		setMe(me);
		createMap(level);
		this.slist = new ScoreList(players);
		slist.setVisible(true);
		keyController = new KeyClass(this, me);
		screen = new Screen(this.level);
		screen.addPlayer(me);
		screen.setVisible(true);
		screen.addKeyListener(keyController);
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
				me.setDirection(direction);
				int x = me.getXpos(), y = me.getYpos();
				
				if (direction.equals(Direction.RIGHT)) {
					x += 1;
				} else 	if (direction.equals(Direction.LEFT)) {
					x -= 1;
				} else if (direction.equals(Direction.UP)) {
					y -= 1;
				} else if (direction.equals(Direction.DOWN)) {
					y += 1;
				}
				
				try {
					if (level[y].charAt(x) != wall) {
						me.setXpos(x);
						me.setYpos(y);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO fixme
					e.printStackTrace();
					System.err.println("You tried to move to " + x + ", " + y + ", but failed");
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
		slist.addPlayer(p);
		p.addObserver(slist);
	}

	// TODO Getplayerid, hvis id'et ikke findes i settet så oprettes spilleren,
	// ellers opdateres der
}