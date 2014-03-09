package game;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScoreList extends JFrame implements PlayerObserver {

	
//	private ArrayList<JLabel> labels = new ArrayList<JLabel>();
	private Map<Player, JLabel> labels = new HashMap<>();
	
	public ScoreList( Collection<Player> players) {
		this();
		if (players != null) {
			for (Player player : players) {
				addPlayer(player);
			}
		}
	}
	
	public ScoreList() {
		super("Scores");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		
		this.setVisible(true);
	}

	
	@Override
	public void update(Player p, int oldX, int oldY) {
		JLabel label = labels.get(p);
		if (label == null) {
			addPlayer(p);
		} else {
			label.setText(formatPlayer(p));
		}
	}
	
	public void addPlayer(Player player) {
		JLabel label = new JLabel();
		this.add(label);
		label.setText(formatPlayer(player));
		labels.put(player, label);
		player.addObserver(this);
	}
	
	public void removePlayer(Player player) {
		JLabel l = labels.remove(player);
		this.remove(l);
		player.removeObserver(this);
	}

	private static String formatPlayer(Player player) {
		return player.getName() + ":  " + player.getPoint();
	}



	
}	
	
