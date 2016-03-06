package ai;

import java.util.Random;

import core.Board;
import core.BoardState;
import core.OthelloColor;
import core.StateObserver;

/**
 * A naive ai - tries to place a disk at random location until it is a valid move
 */
public class SimpleAI implements StateObserver {
	
	private OthelloColor color; 
	private Board board;
		
	public SimpleAI(Board board, OthelloColor color) {
		this.color = color;
		this.board = board;
	}

	@Override
	public void updateState(BoardState s) {
		//My turn
		if(s.ongoing && s.currentPlayer == color) {
			
			//Valid position is between 
		    Random randomGenerator = new Random();
		    int i,j;
		    do {
		    	i = randomGenerator.nextInt(Board.BOARD_SIZE);
		    	j = randomGenerator.nextInt(Board.BOARD_SIZE); 
		    } while(!board.diskPlacementPossible(color, i, j));
		    board.putDisk(color, i, j);
		}
	}
}