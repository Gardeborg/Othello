package core;

import ai.SimpleAI;
import score.ConsoleScore;
import gui.View;
import gui.Controller;

public class Othello {

	public static void main(String args[]) {
		Board board = new Board();
		board.initialize();
		board.setNeighbours();
		
		ConsoleScore consoleScore = new ConsoleScore();
		
		View view = new View();
		//View view2 = new View();
		
		//Controller isn't really necessary, View could add listeners
		//@SuppressWarnings("unused")
		new Controller(view, board);
		//Controller controller2 = new Controller(view2,board);
		
        board.registerStateObserver(view);
        //board.registerStateObserver(view2);
        board.registerScoreObserver(consoleScore);
        
        SimpleAI ai = new SimpleAI(board, OthelloColor.BLACK);
        board.registerStateObserver(ai);
	}
}
