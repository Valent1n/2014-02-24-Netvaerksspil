package game;
import java.util.HashSet;
import java.util.Set;
public class gameplayer {
	
	// Players start values
	//private String playerDirection = "up";

	Player me;
	
	private String wall = "w";
	private KeyClass ko;
	private Set<Player> players;
	ScoreList slist;

	
// level is defined column by column
	private String[][] level;
	// level is defined column by column
	Screen screen; 

	public gameplayer(Player me, ScoreList s, String[][] level) {
		this.level = level;
		players = new HashSet<Player>();
		this.me = me;
		this.slist = s;
		screen = new Screen(level,me.getXpos(),me.getYpos());
		screen.setVisible(true);
		ko = new KeyClass(this);
		screen.addKeyListener(ko); 
	}

	


	public void PlayerMoved(String direction, Player player) {
		//TODO Spilleren med id fra parameter bevæges
		player.direction = direction;
		int x = player.getXpos(),y = player.getYpos();
		if (direction.equals("right")) {
			x = player.getXpos() + 1;
		};
		if (direction.equals("left")) {
			x = player.getXpos() - 1;
		};
		if (direction.equals("up")) {
			y = player.getYpos() - 1;
		};
		if (direction.equals("down")) {
			y = player.getYpos() + 1;
		};
		if (level[x][y].equals(wall)) {
			player.subOnePoint();
			slist.updateScoreOnScreen(player);
		} 
		else {
			player.addOnePoint();
			slist.updateScoreOnScreen(player);
			screen.movePlayerOnScreen(player.getXpos(), player.getYpos(), x, y,player.getDirection());
			player.setXpos(x);
			player.setYpos(y);
		}
	}
}

