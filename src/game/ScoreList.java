package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreList extends JFrame {

	/**
	 * @param args
	 */
	ArrayList<Player> players;
//	Player me;
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();

	
	public ScoreList( ArrayList<Player> players) {
		super("TKgame v. 1.0");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		this.setVisible(true);
		this.players = players;
		draw();
		this.setAlwaysOnTop(true);
	}
	public void draw() {
		for (int j = 0; j < players.size(); j++) {
				JLabel l = new JLabel(players.get(j).ToString());
				l.setSize(50,200);
				this.add(l);
				labels.add(l);
		}	
	}	
	public void updateScoreOnScreen(Player p) {
		int playerno = players.indexOf(p);
		labels.get((playerno)).setText(players.get(playerno).ToString());
	}			
	
	


	
}	
	
