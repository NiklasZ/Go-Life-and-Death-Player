package ai;

//This is the abstract class for all AI types to extend.
import main.Board;
import main.Coordinate;
import main.LegalMoveChecker;

public abstract class AI {

    protected String thoughts;
    protected int movesConsidered;
    protected static int moveDepth = 5;

    //Colour of the AI
    protected int colour;

    //NextMove method to be implemented by subclasses
    public abstract Coordinate nextMove(Board b, LegalMoveChecker lMC) throws AIException;

    //Returns AI's colour
    public int getColour() {
        return this.colour;
    }

    //Returns moves considered by a nextMove call.
    //This counter needs to be set/incremented explicitly in the implementation.
    public int getNumberOfMovesConsidered() {
        return movesConsidered;
    }
    
    public static int getSearchDepth(){return moveDepth;}
    public static void setSearchDepth(int i){moveDepth = i;}
}
