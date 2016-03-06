package core;

public class BoardState {
	
	public OthelloColor disks[][] = new OthelloColor[Board.BOARD_SIZE][Board.BOARD_SIZE];
	public OthelloColor currentPlayer;
	public boolean ongoing = false;
		
	protected void set(Disk[][] disks, OthelloColor currentPlayer, boolean ongoing) {
	
		for(int i = 0; i < Board.BOARD_SIZE; i++) {
			for(int j = 0; j < Board.BOARD_SIZE; j++) {
				this.disks[i][j] = disks[i][j].color();
			}
		}
		this.currentPlayer = currentPlayer;
		this.ongoing = ongoing;
	}
}