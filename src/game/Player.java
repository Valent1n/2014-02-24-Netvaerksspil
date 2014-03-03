package game;

//TODO
// Få player ind i et observer pattern
// Player implementerer subject
// Map implementerer observer

public class Player {
int id;
String name;
int xpos;
int ypos;
int point;
String direction;

public Player (String name, int id) {
	this.name = name;
	xpos = 9;
	ypos = 7;
	point = 0;
	direction = "up";
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
public String getDirection() {
	return direction;
}

public void setDirection(String direction) {
	this.direction = direction;
}

String ToString () {
	return name + "   " + point;
}
void addOnePoint() {
	
	point ++;
}

void subOnePoint() {
	point --;
}
}

