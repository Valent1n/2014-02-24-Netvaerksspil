package game;

public enum Direction {
	UP, 
	DOWN, 
	LEFT, 
	RIGHT;
	
	public static Direction fromString(String s) {
		if (s == null) {
			return null;
		}
		String sl = s.toLowerCase();
		if (sl.equals("up"))
			return UP;
		if (sl.equals("down"))
			return DOWN;
		if (sl.equals("left"))
			return LEFT;
		if (sl.equals("right"))
			return RIGHT;
		return null;
	}
}
