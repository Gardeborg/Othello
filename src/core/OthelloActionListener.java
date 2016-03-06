package core;
import java.awt.event.*;

public class OthelloActionListener implements ActionListener{
	int i; int j;
	Board board;
	
	public OthelloActionListener(Board b, int i, int j) {
		this.i = i;
		this.j = j;
		this.board = b;
	}
	
	public void actionPerformed(ActionEvent e) {
		board.putDisk(board.getActivePlayer(), i, j);
	}
}