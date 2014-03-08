package game;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScoreList extends JFrame implements PlayerObserver {

	
//	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	private Map<Player, JLabel> labels = new HashMap<>();
	
	public ScoreList( Collection<Player> players) {
		super("TKgame v. 1.0");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		
		for (Player player : players) {
			addPlayer(player);
		}
		
		this.setVisible(true);
	}

	
	public void updateScoreOnScreen(Player p) {
		JLabel label = labels.get(p);
		if (label == null) {
			addPlayer(p);
		} else {
			label.setText(formatPlayer(p));
		}
	}
	
	@Override
	public void update(Player player, int oldX, int oldY) {
		updateScoreOnScreen(player);
	}
	
	public void addPlayer(Player player) {
		JLabel label = new JLabel(formatPlayer(player));
		this.add(label);
		labels.put(player, label);
	}
	
	public void removePlayer(Player player) {
		//TODO 
	}

	private String formatPlayer(Player player) {
		//TODO 
		return player.getName() + " " + player.getPoint();
	}



	
}	
	
