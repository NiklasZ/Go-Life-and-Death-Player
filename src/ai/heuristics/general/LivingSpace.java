package ai.heuristics.general;

import ai.Objective;
import main.Board;
import main.Coordinate;
import main.LegalMoveChecker;
import ai.Objective.Action;
import ai.heuristics.Heuristic;
import ai.heuristics.Rating;


/**
 * Counts possible living space for defending group.
 * i.e. potential space where the defending group
 * could create an eye.
 */
public class LivingSpace implements Heuristic {

    private int livingSpaceInitial;
    private int livingSpaceCurrent;

    @Override
    public int assess(Board initialBoard, Board currentBoard, LegalMoveChecker lmc, Objective obj, int colourAI) {

        livingSpaceCurrent = 0;
        livingSpaceInitial = 0;

        // get the coordinate and the colour of the targeted stone
        Coordinate targetStone = obj.getPosition();
        byte attacker;
        byte defender;
        if (initialBoard.get(targetStone.x, targetStone.y) == Board.BLACK) {
            defender = Board.BLACK;
            attacker = Board.WHITE;
        } else {
            defender = Board.WHITE;
            attacker = Board.BLACK;
        }

        // get the action of the AI
        Action aiAction = obj.getAction(colourAI);

        // find number of liberties of the targeted group on the previous board
        boolean[][] visitedInitial = new boolean[initialBoard.getWidth()][initialBoard.getHeight()];
        getLivingSpace(initialBoard, targetStone, defender, attacker, visitedInitial, 1);

        // find number of liberties of the targeted group on the current board
        boolean[][] visitedCurrent = new boolean[currentBoard.getWidth()][currentBoard.getHeight()];
        getLivingSpace(currentBoard, targetStone, defender, attacker, visitedCurrent, 2);

        // if the action is KILL a move is good if the current liberties of the targeted group are less
        // if the returned number is negative the move is bad
        // System.out.println("Initial living space: "+livingSpaceInitial);
        //System.out.println("Current living space: "+livingSpaceCurrent);
        if (aiAction == Action.KILL) {
            return (livingSpaceInitial - livingSpaceCurrent) * Rating.LIVINGSPACE.getValue();
        } // if action is DEFEND a move is good if the current liberties of the targeted group are more
        // if the returned number is negative the move is bad
        else {
            return (livingSpaceCurrent - livingSpaceInitial) * Rating.LIVINGSPACE.getValue();
        }
    }

    private void getLivingSpace(Board board, Coordinate c, byte player, byte otherPlayer, boolean[][] visited, int iteration) {
        // return condition
        if (visited[c.x][c.y] || board.get(c.x, c.y) == otherPlayer) {
            return;
        }

        // mark the coordinate as visited in the boolean array
        visited[c.x][c.y] = true;

        if (board.get(c.x, c.y) == Board.EMPTY_AI || board.get(c.x, c.y) == Board.C_EMPTY) {
            //Checks stone one space away from edge.
            if (c.x < board.getWidth() - 1) {
                if (c.x == 0 && board.get(c.x + 1, c.y) == otherPlayer) {
                    return;
                }
            }
            if (c.x > 0) {
                if (c.x == board.getWidth() - 1 && board.get(c.x - 1, c.y) == otherPlayer) {
                    return;
                }
            }
            if (c.y < board.getHeight() - 1) {
                if (c.y == 0 && board.get(c.x, c.y + 1) == otherPlayer) {
                    return;
                }
            }
            if (c.y > 0) {
                if (c.y == board.getHeight() - 1 && board.get(c.x, c.y - 1) == otherPlayer) {
                    return;
                }
            }
            //Checks surrounding pair.
            if (c.x > 0 && c.x < board.getWidth() - 1) {
                if (board.get(c.x - 1, c.y) == otherPlayer && board.get(c.x + 1, c.y) == otherPlayer) {
                    return;
                }
            }
            if (c.y > 0 && c.y < board.getHeight() - 1) {
                if (board.get(c.x, c.y - 1) == otherPlayer && board.get(c.x, c.y + 1) == otherPlayer) {
                    return;
                }
            }

            // increment the global living space counters
            if (iteration == 1) {
                livingSpaceInitial++;
            }
            if (iteration == 2) {
                livingSpaceCurrent++;
            }

        }

        //out of boundary check + recursion
        // check adjacent to the group positions
        if (c.x > 0) {
            getLivingSpace(board, new Coordinate(c.x - 1, c.y), player, otherPlayer, visited, iteration);
        }
        if (c.x < board.getWidth() - 1) {
            getLivingSpace(board, new Coordinate(c.x + 1, c.y), player, otherPlayer, visited, iteration);
        }
        if (c.y > 0) {
            getLivingSpace(board, new Coordinate(c.x, c.y - 1), player, otherPlayer, visited, iteration);
        }
        if (c.y < board.getHeight() - 1) {
            getLivingSpace(board, new Coordinate(c.x, c.y + 1), player, otherPlayer, visited, iteration);
        }
    }
}
