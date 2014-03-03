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

	


	public void PlayerMoved(String direction, int id) {
		//TODO Spilleren med id fra parameter bevæges
		me.direction = direction;
		int x = me.getXpos(),y = me.getYpos();
		if (direction.equals("right")) {
			x = me.getXpos() + 1;
		};
		if (direction.equals("left")) {
			x = me.getXpos() - 1;
		};
		if (direction.equals("up")) {
			y = me.getYpos() - 1;
		};
		if (direction.equals("down")) {
			y = me.getYpos() + 1;
		};
		if (level[x][y].equals(wall)) {
			me.subOnePoint();
			slist.updateScoreOnScreen(me);
		} 
		else {
			me.addOnePoint();
			slist.updateScoreOnScreen(me);
			screen.movePlayerOnScreen(me.getXpos(), me.getYpos(), x, y,me.getDirection());
			me.setXpos(x);
			me.setYpos(y);
		}
	}
}

