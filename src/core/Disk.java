package core;
class Disk{

	static final int 		nrOfNeighbours 	= 8;
	private Disk[] 			neighbours 		= new Disk[nrOfNeighbours];
	private Position 		position;
	private OthelloColor 	color 			= OthelloColor.EMPTY;
		
	Disk(int i, int j) {
		this.position = new Position(i,j);
	}
	
	Disk(int i, int j, OthelloColor color) {
		this.position = new Position(i,j);
		this.color = color;		
	}
	
	protected void setColor(OthelloColor color)  {
		this.color = color;		
	}
	
	protected boolean isEmpty() {
		return color == OthelloColor.EMPTY;
	}
	
	protected void setNeighbours(Disk[] n) {
		neighbours = n;
	}
	

	protected Disk[] getNeighbours() {
		return neighbours;
	}
	
	/*
	 * @return the neighbour with coordinates i,j, null if not found
	 */
	private Disk getNeighbour(int i, int j) {
		for(Disk neighbour : neighbours) {
			if(neighbour != null && 
			   neighbour.position.i == i && 
			   neighbour.position.j == j) {
				return neighbour;
			}
		}
		return null;
	}
	
	protected static boolean oppositeColor(Disk d1, Disk d2) {
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
	protected Disk findNext(Disk startDisk) {
		int iDiff = this.position.i - startDisk.position.i;
		int jDiff = this.position.j - startDisk.position.j;
		int i = this.position.i + (iDiff == 0 ? iDiff : (iDiff/Math.abs(iDiff)));
		int j = this.position.j + (jDiff == 0 ? jDiff : (jDiff/Math.abs(jDiff)));
		return getNeighbour(i,j);
	}	
	
	protected OthelloColor color() {
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
	
	protected void printNeighbours() {
		for(Disk neighbour : neighbours) {
			System.out.print(neighbour.color + ", ");
		}
	}

}