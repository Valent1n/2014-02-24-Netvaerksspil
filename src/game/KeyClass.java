package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyClass implements KeyListener {
	private GamePlayer g;
	private Player me;
	
	public KeyClass(GamePlayer g, Player me){
		this.g = g;
		this.me = me;
	}

		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == ke.VK_UP) {
				g.PlayerMoved(Direction.UP, me);
				//TODO 
			}

			if (ke.getKeyCode() == ke.VK_DOWN) {
				g.PlayerMoved(Direction.DOWN, me);
			}
			if (ke.getKeyCode() == ke.VK_LEFT) {
				g.PlayerMoved(Direction.LEFT, me);
			}
			if (ke.getKeyCode() == ke.VK_RIGHT) {
				g.PlayerMoved(Direction.RIGHT, me);
			}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {

		}
}