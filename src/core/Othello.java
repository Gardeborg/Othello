package core;

import ai.SimpleAI;
import score.ConsoleScore;
import gui.View;
import gui.Controller;

public class Othello {

	public static void main(String args[]) {
		Board board = new Board();
		board.initialize();
			
		View view = new View();
		board.registerStateObserver(view);
		
		//Controller isn't really necessary, View could add listeners
		//@SuppressWarnings("unused")
		new Controller(view, board);
		        
        ConsoleScore consoleScore = new ConsoleScore();
        board.registerScoreObserver(consoleScore);
        
        //SimpleAI ai = new SimpleAI(board, OthelloColor.BLACK);
        //board.registerStateObserver(ai);
	}
}
