package core;
import java.util.ArrayList;

public class Board implements Observable {
	
	public  ArrayList<Disk> 			diskList 			= new ArrayList<Disk>();
	private OthelloColor 				currentPlayer	 	= OthelloColor.WHITE;
	private boolean 					ongoing 			= true;
	private OthelloScore 				score 				= new OthelloScore();
	private ArrayList<ScoreObserver> 	scoreObservers 		= new ArrayList<ScoreObserver>();
	private ArrayList<StateObserver> 	stateObservers 		= new ArrayList<StateObserver>();
	protected static final int 			BOARD_SIZE 			= 10;
	
	/**
	 * A board has 10*10 disks. The 64 positions in the middle are used as the ordinary playfield
	 * By using a field with 10*10 we avoid a lot of checks when finding disks to turn etc. 
	 */
	public Board() {
		for(int i=0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {	
				Disk d = new Disk(i,j);
				diskList.add(index(i,j),d);
			}
		}		
	}	
	
	public void initialize() {
		diskList.get(index(4,4)).setColor(OthelloColor.WHITE);
		diskList.get(index(4,5)).setColor(OthelloColor.BLACK);
		diskList.get(index(5,4)).setColor(OthelloColor.BLACK);
		diskList.get(index(5,5)).setColor(OthelloColor.WHITE);		
	}
		
	public static int index(int i, int j) {
		return i * BOARD_SIZE + j;
	}
	
	public static int index(Position p) {
		return p.x * BOARD_SIZE + p.y;
	}
	
	public void setNeighbours() {
		for(int i=1; i < 9; i++) {
			for(int j = 1; j < 9; j++) {
				Disk[] neighbours = new Disk[8];
				neighbours[0] = diskList.get(index(i-1,j-1));
				neighbours[1] = diskList.get(index(i-1,j));
				neighbours[2] = diskList.get(index(i-1,j+1));
				neighbours[3] = diskList.get(index(i,j+1));
				neighbours[4] = diskList.get(index(i+1,j+1));
				neighbours[5] = diskList.get(index(i+1,j));
				neighbours[6] = diskList.get(index(i+1,j-1));
				neighbours[7] = diskList.get(index(i,j-1));
				diskList.get(index(i,j)).setNeighbours(neighbours);
			}
		}
	}
	
	public void print() {
		for(int i = 0; i < BOARD_SIZE; i++)	{
			for(int j = 0; j < BOARD_SIZE; j++)	{
				diskList.get(index(i,j)).print();
			}	
			System.out.println();
		}
	}
	
	/**
	 * A player is putting their disk 
	 * @param 	color
	 * @param 	i,j	position using normal 8*8 coordinates
	 * @return 	if the move is valid or not
	 */
	public boolean putDisk(OthelloColor color, int i, int j) {
		if(color != getActivePlayer())
			return false;
		
		Position position = new Position(i,j);
		position.toLogicCoords();
		if(!diskList.get(index(position)).isEmpty()) {
			System.out.println("Occupied!");
			return false; 
		}
		
		boolean validMove = false;
		
		Disk diskToPlace = diskList.get(index(position));
		diskToPlace.setColor(color);
		ArrayList<Disk> disksToSwap = new ArrayList<Disk>();
		
		//Check in all directions for disks to swap
		for(int k = 0; k < Disk.nrOfNeighbours; k++) {
			Disk neighbour = diskList.get(index(position)).getNeighbour(k);
			if(neighbour == null)
				return false;
			ArrayList<Disk> Acc = new ArrayList<Disk>();
			disksToSwap.addAll(getReversibleDisks(diskToPlace,neighbour,Acc));
		}
		//Change the color if found
		if(disksToSwap.size() != 0) {
			for(int k = 0; k < disksToSwap.size(); k++) {
				disksToSwap.get(k).setColor(color);
			}
			updateScore();
			changeTurn();
			notifyScoreObservers();
			notifyStateObservers();
			validMove = true;
		}
		else {
			System.out.println("Not possible to put disk at: " + i +  "- " + j);
			diskToPlace.setColor(OthelloColor.EMPTY);
		}
		return validMove;
	}
	
	private void updateScore() {
		score.white = 0;
		score.black = 0;
		for(int i = 0; i < diskList.size(); ++i) {
			if(diskList.get(i).color() == OthelloColor.WHITE)
				score.white++;
			if(diskList.get(i).color() == OthelloColor.BLACK)
				score.black++;
		}		
	}
	
	//Checks one direction and adds disks of opposite color in acc
	public ArrayList<Disk> getReversibleDisks(Disk diskToPlace, Disk diskToCheck, ArrayList<Disk> acc) {
		if(diskToCheck.isEmpty()) {
			acc.clear();
			return acc;
		}
		if(Disk.oppositeColor(diskToPlace,diskToCheck)) {
			acc.add(diskToCheck);
			Disk nextDiskToCheck = diskList.get(index(diskToCheck.findNext(diskToPlace)));
			return getReversibleDisks(diskToPlace,nextDiskToCheck,acc);
		}
		return acc;
	}
		
	protected OthelloColor getActivePlayer() {
		return currentPlayer;
	}
	
	//Only needed for test
	public void setActivePlayer(OthelloColor color) {
		currentPlayer = color;
	}

	private OthelloColor getPassivePlayer() {
		return currentPlayer.getOpposite();  
	}
	
	/**
	 * Checks if a placement is possible
	 * @param color - Player color
	 * @return true if a placement is possible, false otherwise
	 */
	private boolean placementPossible(OthelloColor player) {
		ArrayList<Disk> acc = new ArrayList<Disk>();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Disk diskToPlace = diskList.get(index(i+1,j+1));
				if(diskToPlace.isEmpty()) {
					//An empty position. 
					for(int k = 0; k < Disk.nrOfNeighbours; k++) {
						Disk neighbour = diskList.get(index(i+1,j+1)).getNeighbour(k);
						//We temporarily place a disk and see if at least one disc would turn.  
						diskToPlace.setColor(player);
						getReversibleDisks(diskToPlace,neighbour,acc);
						diskToPlace.setColor(OthelloColor.EMPTY);
						if(acc.size() != 0)
							return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Only changes the player turn if the player have a valid move
	 */
	private void changeTurn() {
		if(placementPossible(getPassivePlayer())) {
			currentPlayer = getPassivePlayer();
		}
		else if(placementPossible(getActivePlayer())) {
			currentPlayer = getActivePlayer();
		}		
		//No one can (full board is included here)
		else {
			ongoing = false;
		}		
	}
	
	@Override
	public void registerScoreObserver(ScoreObserver o) {
		scoreObservers.add(o);
	}

	@Override
	public void removeScoreObserver(ScoreObserver o) {
		scoreObservers.remove(o);
	}

	@Override
	public void notifyScoreObservers() {
		for(int i = 0; i < scoreObservers.size(); ++i) {
			scoreObservers.get(i).updateScore(score);
		}		
	}
	
	@Override
	public void registerStateObserver(StateObserver o) {
		stateObservers.add(o);
		notifyStateObservers();
	}

	@Override
	public void removeStateObserver(StateObserver o) {
		stateObservers.remove(o);
	}

	@Override
	public void notifyStateObservers() {
		for(int i = 0; i < stateObservers.size(); ++i) {
			stateObservers.get(i).updateState(new BoardState(diskList,currentPlayer,ongoing));
		}		
	}
}