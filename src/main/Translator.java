package main;

import ai.Objective;
import java.util.ArrayList;

/**
 * A static class containing translation operations for the FileIO, TextUI and Objective.
 */
public class Translator {

    //Translates raw lines into GameEngine Object components.
    //It is required to have enough data for a board. It is optional to have any data for the Objective or AI.
    public static GameEngine translateGameInstructions(ArrayList<String> contents) throws BoardFormatException, NumberFormatException {

        Objective objective;
        Board board;
        board = translateToBoard(contents);
        if (contents.size() > 0) {
            objective = translateToObjective(contents.remove(0), board);
        } else {
            System.out.println("NOTE: No objective has been specified. Competitive play disabled.");
            objective = null;
        }
        return new GameEngine(board, objective);
    }

    //Checks the integrity of an inputted board data and translates it to a Board object. Throws an exception when encountering an error. 
    public static Objective translateToObjective(String raw, Board board) throws BoardFormatException, NumberFormatException {
        String[] parts = raw.split(" ");
        int colour;
        String action;
        Coordinate coord;

        if (parts.length != 4) {
            throw new BoardFormatException("ERROR: The given objective is not formatted correctly.");
        }

        switch (parts[0]) {
            case Board.S_BLACK:
                colour = Board.BLACK;
                break;
            case Board.S_WHITE:
                colour = Board.WHITE;
                break;
            default:
                throw new BoardFormatException("ERROR: The given objective colour \'" + parts[0] + "\' is not white or black.");
        }
        switch (parts[1]) {
            case "kill":
                action = parts[1];
                break;
            case "defend":
                action = parts[1];
                break;
            default:
                throw new BoardFormatException("ERROR: The given objective action \'" + parts[0] + "\' is undefined.");
        }

        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[3]);

        if (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
            coord = new Coordinate(x, y);
        } else {
            throw new BoardFormatException("ERROR:  The given objective coordinates are not within the board's dimensions.");
        }

        return new Objective(action, colour, coord);
    }

    //Translates search values for AI and verifies that they are within the given board's length.
    public static int[] translateToSearchValues(String raw, Board board) throws BoardFormatException, NumberFormatException {
        String[] elements = raw.split(" ");

        if (elements.length != 4) {
            throw new BoardFormatException("ERROR: The given search space does not have the required 4 values (x1 y1 x2 y2).");
        }

        int[] boundaries = new int[elements.length];

        for (int i = 0; i < boundaries.length; i++) {
            boundaries[i] = Integer.parseInt(elements[i]);
        }

        if (boundaries[0] < 0 || boundaries[0] > board.getWidth()) {
            throw new BoardFormatException("ERROR: The given search space x1 is not within the board's dimensions.");
        }
        if (boundaries[1] < 0 || boundaries[1] > board.getHeight()) {
            throw new BoardFormatException("ERROR: The given search space y1 is not within the board's dimensions.");
        }
        if (boundaries[2] < 0 || boundaries[2] > board.getWidth()) {
            throw new BoardFormatException("ERROR: The given search space x2 is not within the board's dimensions.");
        }
        if (boundaries[3] < 0 || boundaries[3] > board.getHeight()) {
            throw new BoardFormatException("ERROR: The given search space y2 is not within the board's dimensions.");
        }

        return boundaries;
    }

    //Checks the integrity of an inputted board data and translates it to a Board object. Throws an exception when encountering an error.
    public static Board translateToBoard(ArrayList<String> raw) throws BoardFormatException, NumberFormatException {
        byte[][] rawBoard;
        int w, h;

        if (raw.size() < 2) {
            throw new BoardFormatException("ERROR: The given input does not contain enough lines for a Board.");
        }

        String[] rawInts = raw.remove(0).split(" ");

        if (rawInts.length == 2) {
            w = Integer.parseInt(rawInts[0]);
            h = Integer.parseInt(rawInts[1]);
        } else {
            throw new BoardFormatException("ERROR: The initial line does not contain two valid integers.");
        }

        if (w != h) {
            throw new BoardFormatException("ERROR: The width and height of the board need to be equal.");
        }

        if (h > raw.size()) {
            throw new BoardFormatException("ERROR: There are not enough rows in the specification.");
        }

        for (int i = 0; i < h; i++) {
            if (raw.get(i).length() != w) {
                throw new BoardFormatException("ERROR: There is a mismatch in the given number of columns.");
            }
        }

        rawBoard = new byte[w][h];
        for (int i = 0; i < h; i++) {
            String row = raw.remove(0);
            for (int j = 0; j < w; j++) {
                rawBoard[j][i] = Translator.translateToInt(row.charAt(j));
            }
        }
        return new Board(rawBoard);
    }

    //Translates GameEngine into writable String
    public static String translateToFile(GameEngine gameEngine) throws BoardFormatException {

        Board board = gameEngine.getCurrentBoard();
        Objective objective = gameEngine.getObjective();
        StringBuilder content = new StringBuilder();

        content.append(translateToBoardInstruction(board));

        if (objective != null) {
            content.append(translateToObjectiveInstruction(objective));
        }
        System.out.println(content.toString());
        return content.toString();
    }

    //Translates Board into a writable String
    public static String translateToBoardInstruction(Board board) throws BoardFormatException {
        StringBuilder content = new StringBuilder();
        int w = board.getWidth();
        int h = board.getHeight();
        char[][] cBoard = new char[h][w];
        content.append(w + " " + h + '\n');

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                cBoard[j][i] = Translator.translateToChar(board.get(i, j));
            }
        }

        for (int i = 0; i < h; i++) {
            content.append(cBoard[i]);
            content.append('\n');
        }
        //System.out.println(content.toString());
        return content.toString();

    }

    //Translates Objective into a writable String
    public static String translateToObjectiveInstruction(Objective objective) throws BoardFormatException {
        return translateToString(objective.getStartingColour()) + ' ' + objective.getOriginalAction() + ' '
                + objective.getPosition().x + ' ' + objective.getPosition().y + '\n';
    }

    // Method for translation of char positions to int values
    public static byte translateToInt(char value) throws BoardFormatException {
        switch (value) {
            case Board.C_EMPTY:
                return Board.EMPTY;
            case Board.C_BLACK:
                return Board.BLACK;
            case Board.C_WHITE:
                return Board.WHITE;
            case Board.C_EMPTY_AI:
                return Board.EMPTY_AI;
            default:
                throw new BoardFormatException("ERROR: The board value to translate contains illegal character " + value + ".");
        }
    }

    //Translates Board int elements into chars
    public static char translateToChar(int i) throws BoardFormatException {
        switch (i) {
            case Board.EMPTY:
                return Board.C_EMPTY;
            case Board.BLACK:
                return Board.C_BLACK;
            case Board.WHITE:
                return Board.C_WHITE;
            case Board.EMPTY_AI:
                return Board.C_EMPTY_AI;
            default:
                throw new BoardFormatException("ERROR: The board value to translate contains illegal integer " + i + ".");
        }
    }

    //Translates Board int elements into text
    public static String translateToString(int i) throws BoardFormatException {
        switch (i) {
            case Board.EMPTY:
                return Board.S_EMPTY;
            case Board.BLACK:
                return Board.S_BLACK;
            case Board.WHITE:
                return Board.S_WHITE;
            case Board.C_EMPTY_AI:
                return Board.S_EMPTY;
            default:
                throw new BoardFormatException("ERROR: The board value to translate contains illegal integer " + i + ".");
        }
    }
    //Methods for printing the Board to the console.
    public static void printGameBoard(Board b) {

        byte[][] board = b.getRaw();
        ArrayList<String> lines = new ArrayList<>();

        try {
            for (int i = 0; i < board[0].length; i++) {
                lines.add("");
                int p = lines.size() - 1;
                for (int j = 0; j < board.length; j++) {
                    lines.set(p, lines.get(p) + Translator.translateToChar(board[j][i]));
                }
            }
            printBoard(lines);
        } catch (BoardFormatException bad) {
            System.err.println(bad.getMsg() + "\n> The board could not be printed");
        }
    }

    //Prints general boards.
    private static void printBoard(ArrayList<String> lines) {

        System.out.println();
        lines = addBoardDetails(lines);

        for (String s : lines) {
            System.out.println(s);
        }

        System.out.println();

    }

    //Adds details to a board view. Currently just board indexing.
    private static ArrayList<String> addBoardDetails(ArrayList<String> board) {

        //indices
        int width = board.get(0).length();
        int height = board.size();
        int wLength = String.valueOf(width).length(); //Digit length for buffering.
        int hLength = String.valueOf(height).length();

        ArrayList<String> detailedBoard = new ArrayList<>();

        //Row indices
        for (int i = 0; i < height; i++) {

            String spacing = "";
            int buffer = String.valueOf(i).length();

            while (buffer++ < hLength) {
                spacing += ' ';
            }

            spacing += Integer.toString(i) + ' ';
            detailedBoard.add(spacing + board.get(i));
        }

        detailedBoard.add("");

        //Column indices
        for (int i = 0; i < wLength; i++) {

            String line = "";
            for (int j = 0; j < width; j++) {
                if (String.valueOf(j).length() > i) {
                    line += Integer.toString(j).charAt(i);
                } else {
                    line += ' ';
                }
            }
            for (int j = 0; j < hLength; j++) {
                line = ' ' + line;
            }
            detailedBoard.add(' ' + line);
        }
        return detailedBoard;
    }
}
