package game;

import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen  extends JFrame implements PlayerObserver {
	private static final long serialVersionUID = -4787560479216301718L;
	private static final String nameOfTheGame = "Arnold Schwarzenegger!";
	private static final ImageIcon wIcon = new ImageIcon("./Image/mur1.png");
	private static final ImageIcon eIcon = new ImageIcon("./Image/gulv2.png");
	private static final int fieldSizeX = 50, fieldSizeY = 50;
	private static final ImageIcon[] heroIcons = new ImageIcon[] {
		new ImageIcon("./Image/HeltOp.png"),		//this must follow the same order as in the 
		new ImageIcon("./Image/HeltNed.png"),		//game.Direction enum
		new ImageIcon("./Image/Heltvenstre.png"),
		new ImageIcon("./Image/Helthoejre.png")
	};
			
	private JLabel[][] labels;
	
	private String[] level;
	int dimX, dimY;
	
	
	public Screen(String[] level)
	{
		super(nameOfTheGame);
		this.level = level;
		dimY = level.length;
		dimX = level[0].length();
		this.labels = new JLabel[dimY][dimX];
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(500, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(dimX, dimY, 0, 0));
		init();
//		this.setAlwaysOnTop(true);
		this.repaint();
		this.setVisible(true);
	}
	@Override
	public void update(Player player, int oldX, int oldY) {
		movePlayerOnScreen(oldX, oldY, player);
		
	}
	
	public void addPlayer(Player player) {
		drawField(player);
		player.addObserver(this);
	}
	
	public void removePlayer(Player p) {
		int x = p.getXpos(), y = p.getYpos();
		drawField(x, y);
	}
	
	public void init(Collection<Player> players) {
		init();
		for (Player p : players) {
			addPlayer(p);
		}
	}
	
	private void movePlayerOnScreen(int oldX, int oldY, Player p) {
		
		drawField(oldX, oldY);
		
		drawField(p);
	}
	
//	
//	private void drawField(int x, int y, ImageIcon icon) {
//		labels[y][x].setIcon(icon);
//	}
	
	/**
	 * Draws an empty field at the specified position
	 */
	private void drawField(int x, int y) {
		if (x >= 0 && x < dimX && y >= 0 && y < dimY ) {
			ImageIcon icon;
			switch (level[y].charAt(x)) {
			case 'e':
				icon = eIcon;
				break;
			case 'w':
			default:
				icon = wIcon;
				break;
			}
			labels[y][x].setIcon(icon);
		}
	}
	
	private void drawField(Player p) {
		int x = p.getXpos(), y = p.getYpos();
		Direction d = p.getDirection();
		
		if (x >= 0 && x < dimX && y >= 0 && y < dimY && d != null) {
			ImageIcon icon = heroIcons[d.ordinal()];
			labels[y][x].setIcon(icon);
		}
	}
	
	
	private void init() {
		for (int y = 0; y < dimY; y++) {
			for (int x = 0; x < dimX; x++) {
				JLabel field;
				ImageIcon icon;
				switch (level[y].charAt(x)) {
				case 'e':
					icon = eIcon;
					break;
				case 'w':
				default:
					icon = wIcon;
					break;
				}
				field = new JLabel();
				field.setIcon(icon);
				field.setSize(fieldSizeX, fieldSizeY);
				this.add(field);
				labels[y][x] = field;
				
			}
		}
	}
}
