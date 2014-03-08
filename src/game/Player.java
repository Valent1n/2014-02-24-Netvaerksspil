package game;

import java.util.ArrayList;
import java.util.List;


public class Player {
	private String name;
	private final int id;
	private int xpos;
	private int ypos;
	private int point;
	private Direction direction;
	private List<PlayerObserver> observers;
	
	public Player(String name, int id, int xpos, int ypos, int score, Direction direction) {
		this.id = id;
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		point = score;
		this.direction = direction;
		observers = new ArrayList<>();
	}
	
	public Player(String name, int id, int xpos, int ypos) {
		this(name, id, xpos, ypos, 0, Direction.RIGHT);
	}


	public int getXpos() {
		return xpos;
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
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
