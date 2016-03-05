package core;
import java.awt.event.*;

public class OthelloActionListener implements ActionListener{
	int i; int j;
	Board b;
	
	public OthelloActionListener(Board b, int i, int j) {
		this.i = i;
		this.j = j;
		this.b = b;
	}
	
	public void actionPerformed(ActionEvent e) {
		b.putDisk(b.getActivePlayer(), i, j);
	}
}