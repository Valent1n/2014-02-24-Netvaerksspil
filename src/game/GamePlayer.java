package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayer {

	// Players start values
	// private String playerDirection = "up";

	Player me;

	private String wall = "w";
	private KeyClass ko;
	private List<Player> players;
	private Network network;

	ScoreList slist;

	// level is defined column by column
	private String[][] level = {
			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
					"w", "w", "w", "w", "w", "w", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "e",
					"e", "e", "e", "e", "e", "e", "w" },
			{ "w", "e", "w", "e", "e", "w", "e", "e", "w", "w", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "e", "w", "e", "e", "e", "w", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "e", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "w", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "e", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "e", "w", "e", "w", "e", "e", "w", "e", "e", "w",
					"e", "e", "w", "e", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "w", "e", "e", "w", "e", "e", "w",
					"e", "e", "w", "e", "e", "e", "w" },
			{ "w", "e", "w", "w", "e", "w", "w", "e", "e", "e", "e", "e", "e",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "w", "e", "w", "e", "e", "e", "e", "w", "e", "e",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "w",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "e", "e",
					"e", "e", "w", "e", "e", "w", "w" },
			{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "e", "w", "e", "w", "w", "w", "e", "e", "w", "e", "w",
					"e", "e", "w", "w", "e", "w", "w" },
			{ "w", "e", "w", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "e", "e", "w", "e", "e", "e", "w", "w", "e", "e", "w",
					"e", "e", "e", "e", "e", "e", "w" },
			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
					"w", "w", "w", "w", "w", "w", "w" }, };
	// level is defined column by column
	Screen screen;

	public GamePlayer(Player me, ScoreList s, String[][] level, Network network) {
		this.network = network;
		players = new ArrayList<Player>();
		this.level = level;
		this.me = me;
		players.add(me);
		this.slist = s;
		screen = new Screen(level, me.getXpos(), me.getYpos());
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
		screen = new Screen(level, me.getXpos(), me.getYpos());
		screen.setVisible(true);
		ko = new KeyClass(this, me);
		screen.addKeyListener(ko);

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
		if (level[x][y].equals(wall)) {
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
		slist.updateScoreOnScreen(p);
	}

	// TODO Getplayerid, hvis id'et ikke findes i settet så oprettes spilleren,
	// ellers opdateres der
}
