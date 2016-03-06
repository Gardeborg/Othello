package gui;

import core.Board;
import core.OthelloActionListener;

public class Controller {
	
	private View view;
	private Board board;
	
	public Controller(View v, Board b) {
		this.view = v;
		this.board = b;
		
		for(int i = 0; i < Board.BOARD_SIZE; ++i) {
			for(int j = 0; j < Board.BOARD_SIZE; ++j) {
				view.addButtonListener(i, j, new OthelloActionListener(board, i, j));
			}
		}
	}
}
