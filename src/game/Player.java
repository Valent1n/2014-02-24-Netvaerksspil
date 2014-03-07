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
	
	public Player(String name, int id, int xpos, int ypos) {
		this.id = id;
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		point = 0;
		direction = Direction.UP;
		observers = new ArrayList<>();
		observationPaused = false;
		changedSinceLastNotify = false;
	}


	public int getXpos() {
		return xpos;
	}
	

	public void setId(int id) {
		this.id = id;
	}


	public void setXpos(int xpos) {
		if (xpos != this.xpos) {
			this.xpos = xpos;
			notifyObservers();
		}
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		if (ypos != this.ypos) {
			this.ypos = ypos;
			notifyObservers();
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		if (direction != this.direction) {
			this.direction = direction;
			notifyObservers();
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
			notifyObservers();
		}
	}

	public void addOnePoint() {
		point++;
		notifyObservers();
	}

	public void subOnePoint() {
		point--;
		notifyObservers();
	}
	
	public void addPoints(int points) {
		point += points;
		if (points != 0) {
			notifyObservers();
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
	
	private void notifyObservers() {
		if (observationPaused) {
			changedSinceLastNotify = true;
			
		} else {
			List<PlayerObserver> copy;
			synchronized (observers) {
				copy = new ArrayList<>(observers);
			}
			for (PlayerObserver po : copy) {
				po.update(this);
			}
			changedSinceLastNotify = false;
		}
	}
	public void pauseObservation() {
		observationPaused = true;
	}
	public void unpauseObservation() {
		observationPaused = false;
		if (changedSinceLastNotify) {
			notifyObservers();
		}
	}
	
	
}
