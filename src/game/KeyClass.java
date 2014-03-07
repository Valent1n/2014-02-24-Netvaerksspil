package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyClass implements KeyListener {
	private GamePlayer g;
	
	public KeyClass(GamePlayer g, Player me){
		this.g = g;
	}

	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == ke.VK_UP) {
			g.moveMe(Direction.UP);
			// TODO
		}

		if (ke.getKeyCode() == ke.VK_DOWN) {
			g.moveMe(Direction.DOWN);
		}
		if (ke.getKeyCode() == ke.VK_LEFT) {
			g.moveMe(Direction.LEFT);
		}
		if (ke.getKeyCode() == ke.VK_RIGHT) {
			g.moveMe(Direction.RIGHT);
		}
		if(ke.getKeyCode() == ke.VK_ESCAPE) {
			System.out.println(g.getNetwork());
			g.logOff();
		}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {
			
		}
}