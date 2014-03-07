package game;

import java.awt.GridLayout;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen  extends JFrame implements PlayerObserver {
	private static final String nameOfTheGame = "Arnold Schwarzenegger!";
	private static final String wIcon = "./Image/mur1.png";
	private static final String eIcon = "./Image/gulv2.png";
	private static final int fieldSizeX = 50, fieldSizeY = 50;
	private static final String[] heroIcons = new String[] {
		"./Image/HeltOp.png",
		"./Image/HeltNed.png",
		"./Image/Heltvenstre.png",
		"./Image/Helthoejre.png"
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
	public void movePlayerOnScreen(int oldX, int oldY, int x, int y,Direction playerDirection) {
		if (!(oldX < 0 || oldY < 0)) {
			
			labels[oldY][oldX].setIcon(new ImageIcon(eIcon));
		}
		if ((! (x < 0 || y < 0)) && playerDirection != null) {
			labels[y][x].setIcon(new ImageIcon(heroIcons[playerDirection.ordinal()]));
		}
	}
	
	
	public void addPlayer(Player player) {
		if (! (player.getXpos() < 0 || player.getYpos() < 0)) {
			labels[player.getXpos()][player.getYpos()]
					.setIcon(new ImageIcon(heroIcons[player.getDirection().ordinal()]));
		}
		player.addObserver(this);
	}
	
	public void removePlayer(int posX, int posY) {
		//TODO 
	}
	
	private void init() {
		for (int y = 0; y < dimY; y++) {
			for (int x = 0; x < dimX; x++) {
				JLabel field;
				String icon;
				switch (level[y].charAt(x)) {
				case 'e':
					icon = eIcon;
					break;
				case 'w':
				default:
					icon = wIcon;
					break;
				}
				field = new JLabel(new ImageIcon(icon));
				field.setSize(fieldSizeX, fieldSizeY);
				this.add(field);
				labels[y][x] = field;
				
			}
		}
	}
	@Override
	public void update(Player player, int oldX, int oldY) {
		movePlayerOnScreen(oldX, oldY, player.getXpos(), player.getYpos(), player.getDirection());
		
	}
}
