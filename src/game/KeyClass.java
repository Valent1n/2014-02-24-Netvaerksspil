package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyClass implements KeyListener {
	private GamePlayer g;
	
	public KeyClass(GamePlayer g, Player me){
		this.g = g;
	}

	public void keyPressed(KeyEvent ke) {
		int keyCode = ke.getKeyCode();
		
		if (keyCode == KeyEvent.VK_UP) {
			g.moveMe(Direction.UP);
		} else if (keyCode == KeyEvent.VK_DOWN) {
			g.moveMe(Direction.DOWN);
		} else if (keyCode == KeyEvent.VK_LEFT) {
			g.moveMe(Direction.LEFT);
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			g.moveMe(Direction.RIGHT);
		} else if(keyCode == KeyEvent.VK_ESCAPE) {
			System.out.println(g.getNetwork());
			g.logOff();
		}
	}

		public void keyReleased(KeyEvent ke) {
			//Do Nothing
		}

		public void keyTyped(KeyEvent arg0) {
			//Do Nothing
		}
}