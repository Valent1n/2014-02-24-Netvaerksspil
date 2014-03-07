package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Player {
	private String name;
	private int id;
	private int xpos;
	private int ypos;
	private int point;
	private Direction direction;
	private List<PlayerObserver> observers;
	private boolean observationPaused;
	private boolean changedSinceLastNotify;
	
	public Player(String name, int id, int xpos, int ypos, int score, Direction direction) {
		this.id = id;
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		point = score;
		this.direction = direction;
		observers = new ArrayList<>();
		observationPaused = false;
		changedSinceLastNotify = false;
	}
	
	public Player(String name, int id, int xpos, int ypos) {
		this(name, id, xpos, ypos, 0, Direction.RIGHT);
	}


	public int getXpos() {
		return xpos;
	}
	

	public void setId(int id) {
		this.id = id;
	}


	public void setXpos(int xpos) {
		if (xpos != this.xpos) {
			int oldX = xpos;
			this.xpos = xpos;
			notifyObservers(oldX, ypos);
		}
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		if (ypos != this.ypos) {
			int oldY = ypos;
			this.ypos = ypos;
			notifyObservers(xpos, oldY);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if (direction != this.direction) {
			this.direction = direction;
			notifyObservers(xpos, ypos);
		}
	}

	public String toString() {
		return name + "   " + point;
	}
	

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		if (point != this.point) {
			this.point = point;
			notifyObservers(xpos, ypos);
		}
	}

	public void addOnePoint() {
		point++;
		notifyObservers(xpos, ypos);
	}

	public void subOnePoint() {
		point--;
		notifyObservers(xpos, ypos);
	}
	
	public void addPoints(int points) {
		point += points;
		if (points != 0) {
			notifyObservers(xpos, ypos);
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void addObserver(PlayerObserver po) {
		synchronized (observers) {
			if (!observers.contains(po)) {
				observers.add(po);
			}
		}
	}
	public void removeObserver(PlayerObserver po) {
		synchronized (observers) {
			while (observers.remove(po)) {
				//nothing
			}
		}
	}
	
	private void notifyObservers(int oldX, int oldY) {
		
		List<PlayerObserver> copy;
		synchronized (observers) {
			copy = new ArrayList<>(observers);
		}
		for (PlayerObserver po : copy) {
			po.update(this, oldX, oldY);
		}
		changedSinceLastNotify = false;
	}
	
	
	
}
