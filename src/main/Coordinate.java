package main;

public class Coordinate {

    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.x) + " ," + String.valueOf(this.y);
    }
}
