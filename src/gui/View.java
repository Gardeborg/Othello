package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


//import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import core.Board;
import core.BoardState;
import core.OthelloColor;
import core.StateObserver;

public class View implements StateObserver {
	
	private JButton[][] button = new JButton[Board.BOARD_SIZE][Board.BOARD_SIZE];
	private ImageIcon whiteIcon = new ImageIcon(this.getClass().getResource("white.png"));
	private ImageIcon blackIcon = new ImageIcon(this.getClass().getResource("black.png"));
	private JFrame frame;
	
	public View() {

		frame = new JFrame("Othello");
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 600));
		
		addComponentsToPane(frame);
	    createMenuBar(frame);
	    
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    //Display the window.
	    frame.setVisible(true);
	}
	
	private void createMenuBar(Container contentPane) {

	    JMenuBar menuBar = new JMenuBar();
	    
	    //Build the first menu.
	    JMenu menu = new JMenu("File");
	    menu.setMnemonic(KeyEvent.VK_F);
	    menu.getAccessibleContext().setAccessibleDescription(
	            "The only menu in this program that has menu items");
	    
	    //New
	    JMenuItem menuNew = new JMenuItem("New",
		                KeyEvent.VK_T);
	    menuNew.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
	    menuNew.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu.add(menuNew);
		
		//Save
		JMenuItem menuSave = new JMenuItem("Save game");
		menu.add(menuSave);
		
		//Load
		JMenuItem menuLoad = new JMenuItem("Load game");
		menu.add(menuLoad);
		
		//Quit
		JMenuItem menuQuit  = new JMenuItem("Quit");
		menu.add(menuQuit);
		
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

	}
	
	private void addComponentsToPane(Container contentPane) {
		contentPane.setLayout(new GridLayout(Board.BOARD_SIZE,Board.BOARD_SIZE));
		for(int i = 0; i < Board.BOARD_SIZE; i++) {
			for(int j = 0; j < Board.BOARD_SIZE; j++) {
				JButton tempButton = new JButton();
				button[i][j] = tempButton;
				contentPane.add(tempButton);
			}
		}
	}
	
	private void updateGui(BoardState s) {
		for(int i = 0; i < Board.BOARD_SIZE; i++) {
			for(int j = 0; j < Board.BOARD_SIZE; j++) {
				if(s.disks[i][j] == OthelloColor.WHITE) {
					button[i][j].setIcon(whiteIcon);
				}
				else if(s.disks[i][j] == OthelloColor.BLACK) {
					button[i][j].setIcon(blackIcon);
				}
			}
	    }
		
	    String player;
	    if(s.currentPlayer == OthelloColor.WHITE)
	    	player = "White's turn";
	    else
	    	player = "Black's turn";
	    
	    frame.setTitle(player);
	}
	
	public void addButtonListener(int i, int j, ActionListener listener) {
		button[i][j].addActionListener(listener);
	}

	@Override
	public void updateState(BoardState s) {
		updateGui(s);
		if(!s.ongoing) {
			JOptionPane.showMessageDialog(frame, "Game ended!",
					"Good Job both of you", JOptionPane.PLAIN_MESSAGE);			
		}
	}	
}