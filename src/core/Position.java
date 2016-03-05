package core;

public class Position {
	public int x,y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void toLogicCoords() {
		this.x += 1;
		this.y += 1;
	}
}
