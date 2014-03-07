package game;

import java.awt.GridLayout;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen  extends JFrame {
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
	
		labels[oldY][oldX].setIcon(new ImageIcon(eIcon));

		labels[y][x].setIcon(new ImageIcon(heroIcons[playerDirection.ordinal()]));
	}
	
	public void addPlayer(int posX, int posY, Direction direction) {
		labels[posY][posX].setIcon(new ImageIcon(heroIcons[direction.ordinal()]));
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
}
