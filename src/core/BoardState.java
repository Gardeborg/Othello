package core;

import java.util.ArrayList;

//TODO create setters/getters
public class BoardState {
	
	public ArrayList<Disk> diskList = new ArrayList<Disk>();
	public OthelloColor currentPlayer;
	public boolean ongoing = false;
		
	public BoardState(ArrayList<Disk> d, OthelloColor currentPlayer, boolean ongoing) {
		this.diskList = d;
		this.currentPlayer = currentPlayer;
		this.ongoing = ongoing;
	}
}