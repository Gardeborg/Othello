package core;
public enum OthelloColor{
	WHITE,
	BLACK,
	EMPTY;
	
    private OthelloColor opposite;

    static {
        WHITE.opposite = BLACK;
        BLACK.opposite = WHITE;
        EMPTY.opposite = EMPTY;
    }

    public OthelloColor getOpposite() {
        return opposite;
    }
}

