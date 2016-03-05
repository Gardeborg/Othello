package ai;

import java.util.Random;

import core.Board;
import core.BoardState;
import core.OthelloColor;
import core.StateObserver;

public class SimpleAI implements StateObserver {
	
	private OthelloColor color; 
	private Board board;
		
	//Change board to an interface instead?
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
		    	i = randomGenerator.nextInt(8);
		    	j = randomGenerator.nextInt(8); 
		    } while(!board.putDisk(color, i, j));
		}
	}
	
	
	
	
	
	
	

}
