package core;
public class Disk{

	static final int nrOfNeighbours = 8;
	private Disk[] neighbours = new Disk[nrOfNeighbours];
	private Position position;
	private OthelloColor color = OthelloColor.EMPTY;
		
	Disk(int i, int j) {
		this.position = new Position(i,j);
	}
	
	Disk(int i, int j, OthelloColor color) {
		this.position = new Position(i,j);
		this.color = color;		
	}
	
	public void setPos(int i, int j) {
		this.position.x = i;
		this.position.y = j;
	}
	
	public void setColor(OthelloColor color)  {
		this.color = color;		
	}
	
	public boolean isEmpty() {
		return color == OthelloColor.EMPTY;
	}
	
	public void setNeighbours(Disk[] n) {
		neighbours = n;
	}
	
	public void printNeighbours() {
		for(int i = 0; i < nrOfNeighbours; i++)
			System.out.print(neighbours[i].color + ", ");
	}
	
	public Disk getNeighbour(int i) {
		return neighbours[i];
	}
	
	public static boolean oppositeColor(Disk d1, Disk d2) {
		if(d1.color == d2.color)
			return false;
		else 
			return true;
	}
	
	/*
	 * Calculates the position of the location further away from the 
	 * current disk in relation to the start disk.
	 * Example: start disk is at location 0,1, current at 0,3. Position is at 0,4
	 * 
	 * @param 	the starting disk
	 * @return 	the position
	 */	
	protected Position findNext(Disk startDisk) {
		int iDiff = this.position.x - startDisk.position.x;
		int jDiff = this.position.y - startDisk.position.y;
		int i = this.position.x + (iDiff == 0 ? iDiff : (iDiff/Math.abs(iDiff)));
		int j = this.position.y + (jDiff == 0 ? jDiff : (jDiff/Math.abs(jDiff)));
		return new Position(i,j);
	}
	
	public OthelloColor color() {
		return color;
	}
	
	protected void print() {
		if(color == OthelloColor.WHITE)
			System.out.print("w");
		else if(color == OthelloColor.EMPTY)
			System.out.print(0);
		else
			System.out.print("b");
	}
}