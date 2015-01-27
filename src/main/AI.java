package main;

//This is the abstract class for all AI types to extend.
public abstract class AI {

    //These bounds define the space that should be searched by the AI on the board. It is ensured beforehand, that they are within the board.
    protected int lowerBoundX;
    protected int upperBoundX;
    protected int lowerBoundY;
    protected int upperBoundY;

    //Colour of the AI
    protected int colour;

    //NextMove method to be implemented by subclasses
    public abstract Coordinate nextMove(Board b, LegalMoveChecker lMC);

    //Sets the bounds for the AI to search in. Notably, the input should be in the (x1, y1, x2, y2) format.
    public void setBounds(int[] data) {
        if (data[0] > data[2]) {
            upperBoundX = data[0];
            lowerBoundX = data[2];
        } else {
            upperBoundX = data[2];
            lowerBoundX = data[0];
        }
        if (data[1] > data[3]) {
            upperBoundY = data[1];
            lowerBoundY = data[3];
        } else {
            upperBoundY = data[3];
            lowerBoundY = data[1];
        }
    }
    
    //Returns AI's colour
    public int getColour() {
        return this.colour;
    }
}
