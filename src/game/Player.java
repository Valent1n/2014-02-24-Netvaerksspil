package game;

//TODO
// Få player ind i et observer pattern
// Player implementerer subject
// Map implementerer observer

public class Player {
	private int id;
	private String name;
	private int xpos;
	private int ypos;
	private int point;
	private Direction direction;

	
	public Player(String name, int id) {
		this.id = id;
		this.name = name;
		xpos = 9;
		ypos = 7;
		point = 0;
		direction = Direction.UP;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String toString() {
		return name + "   " + point;
	}
	

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public void addOnePoint() {

		point++;
	}

	public void subOnePoint() {
		point--;
	}
	
	public void addPoints(int points) {
		point += points;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
