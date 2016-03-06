package core;
import java.util.ArrayList;

public class Board implements Observable {
	
	public static final int 			BOARD_SIZE 			= 8;
	private	Disk						disks[][]           = new Disk[BOARD_SIZE][BOARD_SIZE]; 
	private OthelloColor 				currentPlayer	 	= OthelloColor.WHITE;
	private boolean 					ongoing 			= true;
	private OthelloScore 				score 				= new OthelloScore();
	private BoardState	 				state 				= new BoardState();
	private ArrayList<ScoreObserver> 	scoreObservers 		= new ArrayList<ScoreObserver>();
	private ArrayList<StateObserver> 	stateObservers 		= new ArrayList<StateObserver>();
	
	
	/**
	 * Creates a new board. 
	 */
	public Board() {
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {	
				disks[i][j] = new Disk(i,j);
			}
		}
	}
	
	/**
	 * Initializes the board with the starting disks.
	 */
	public void initialize() {
		setNeighbours();
		int low = (BOARD_SIZE - 1)/2;
		int high = (BOARD_SIZE)/2;
		disks[low][low].setColor(OthelloColor.WHITE);
		disks[low][high].setColor(OthelloColor.BLACK);
		disks[high][low].setColor(OthelloColor.BLACK);
		disks[high][high].setColor(OthelloColor.WHITE);
	}
			
	/*
	 * A disk have 8 neighbours. A disk on the edge have some neighbours which are null.  
	 */
	private void setNeighbours() {
		for(int j = 0; j < BOARD_SIZE; j++) {
			for(int i = 0; i < BOARD_SIZE; i++) {
				Disk neighbours[] = new Disk[8];
				neighbours[0] = ((i - 1) < 0 || (j - 1) < 0) ? null : disks[i-1][j-1];
				neighbours[1] = ((i - 1) < 0) ? null : disks[i-1][j];
				neighbours[2] = ((i - 1) < 0 || (j + 1) == BOARD_SIZE) ? null : disks[i-1][j+1];
				neighbours[3] = ((j + 1) == BOARD_SIZE) ? null : disks[i][j+1];
				neighbours[4] = ((i + 1) == BOARD_SIZE || (j + 1) == BOARD_SIZE) ? null : disks[i+1][j+1];
				neighbours[5] = ((i + 1) == BOARD_SIZE) ? null : disks[i+1][j];
				neighbours[6] = ((i + 1) == BOARD_SIZE || (j-1) < 0) ? null : disks[i+1][j-1];
				neighbours[7] = ((j - 1) < 0) ? null : disks[i][j-1];
				disks[i][j].setNeighbours(neighbours);
			}
		}
	}
	
	/**
	 * A player is putting their disk. Turns disks and notifies observers
	 * @param 	color 
	 * @param 	i,j	position
	 * @return 	if the move is valid or not
	 */
	public boolean putDisk(OthelloColor color, int i, int j) {
		if(color != getActivePlayer())
			return false;
		
		if(!disks[i][j].isEmpty()) {
			System.out.println("Occupied!");
			return false; 
		}
				
		Disk diskToPlace = disks[i][j];
		diskToPlace.setColor(color);
		ArrayList<Disk> disksToSwap = new ArrayList<Disk>();
		
		//Check in all directions for disks to swap
		for(Disk neighbour : diskToPlace.getNeighbours()) {
			if(neighbour == null)
				continue;
			ArrayList<Disk> acc = new ArrayList<Disk>();
			disksToSwap.addAll(getReversibleDisks(diskToPlace,neighbour,acc));
		}
		boolean validMove = false;
		//Change the color on the reversible disks
		if(disksToSwap.size() != 0) {
			for(Disk diskToSwap : disksToSwap) {
				diskToSwap.setColor(color);
			}
			updateScore();
			changeTurn();
			notifyScoreObservers();
			notifyStateObservers();
			validMove = true;
		}
		else {
			diskToPlace.setColor(OthelloColor.EMPTY);
		}
		return validMove;
	}	
	
	private void updateScore() {
		score.white = 0;
		score.black = 0;

		for(int i = 0; i < BOARD_SIZE; ++i) {
			for(int j = 0; j < BOARD_SIZE; ++j) {
				if(disks[i][j].color() == OthelloColor.WHITE)
					score.white++;
				if(disks[i][j].color() == OthelloColor.BLACK)
					score.black++;
			}
		}
	}
	
	/*
	 * Recursively checks one direction and adds disks of opposite color in acc
	 * @return a list of reversible disks
	 */
	public ArrayList<Disk> getReversibleDisks(Disk diskToPlace, Disk diskToCheck, ArrayList<Disk> acc) {
		if(diskToCheck == null || diskToCheck.isEmpty()) {
			acc.clear();
			return acc;
		}
		if(Disk.oppositeColor(diskToPlace,diskToCheck)) {
			acc.add(diskToCheck);
			Disk nextDiskToCheck = diskToCheck.findNext(diskToPlace);
			return getReversibleDisks(diskToPlace, nextDiskToCheck, acc);
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
	
	/*
	 * Checks if a placement is possible
	 * @param color - Player color
	 * @return true if a placement is possible, false otherwise
	 */
	private boolean placementPossible(OthelloColor player) {
		ArrayList<Disk> acc = new ArrayList<Disk>();
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				Disk diskToPlace = disks[i][j];
				if(diskToPlace.isEmpty()) {
					//An empty position. 
					for(int k = 0; k < Disk.nrOfNeighbours; k++) {
						Disk neighbour = diskToPlace.getNeighbour(k);
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
	
	public boolean diskPlacementPossible(OthelloColor player, int i, int j) {
		ArrayList<Disk> acc = new ArrayList<Disk>();
		Disk diskToPlace = disks[i][j];
		if(diskToPlace.isEmpty()) {
			//An empty position. 
			for(int k = 0; k < Disk.nrOfNeighbours; k++) {
				Disk neighbour = diskToPlace.getNeighbour(k);
				//We temporarily place a disk and see if at least one disc would turn.  
				diskToPlace.setColor(player);
				getReversibleDisks(diskToPlace,neighbour,acc);
				diskToPlace.setColor(OthelloColor.EMPTY);
				if(acc.size() != 0)
					return true;
			}
		}
		return false;
	}
	
	/*
	 * Only changes the player turn if the player has a valid move
	 */
	private void changeTurn() {
		if(placementPossible(getPassivePlayer())) {
			currentPlayer = getPassivePlayer();
		}
		else if(placementPossible(getActivePlayer())) {
			currentPlayer = getActivePlayer();
		}		
		//No one can, game over (full board is included here)
		else {
			ongoing = false;
		}		
	}
	
	/*
	 * Prints the board to console
	 */
	public void print() {
		for(int i = 0; i < BOARD_SIZE; i++)	{
			for(int j = 0; j < BOARD_SIZE; j++)	{
				disks[i][j].print();
			}	
			System.out.println();
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
		state.set(disks, currentPlayer, ongoing);
		for(int i = 0; i < stateObservers.size(); ++i) {
			stateObservers.get(i).updateState(state);
		}		
	}
}