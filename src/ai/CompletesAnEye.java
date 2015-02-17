package ai;

import java.util.ArrayList;
import java.util.Arrays;

import main.Board;
import main.LegalMoveChecker;
import ai.Rating;
//create a first heuristic to detect eyes and 
// return appropriate value
public class CompletesAnEye implements Heuristic{
	
	
	
	@Override
	/// gives a board a move, obj and player to move 
	// should return COMPLETES_AN_EYE if a move
	// is found to return an eye else 0
	public int assess(Board b, LegalMoveChecker lmc, Objective obj, int colourAI) {
		
		int x_limit = b.getWidth();
		int y_limit = b.getHeight();
		// variable for determining how many out of 4 stones are in place
		// to create an eye (x+1,y),(x-1,y),(x,y-1) and (x,y+1)
		int eye_counter = 0;
		byte[][] board_to_assess = b.getRaw();
		//iterate over the board 
		for (int x = 0; x < x_limit;x++){
			for (int y = 0; y < y_limit; y++){
				
				if(board_to_assess[x+1][y] == colourAI )
					eye_counter++;
				if(board_to_assess[x-1][y] == colourAI )
					eye_counter++;
				if(board_to_assess[x][y+1] == colourAI )
					eye_counter++;
				if(board_to_assess[x][y-1] == colourAI )
					eye_counter++;
			}
		}
			
		if (eye_counter == 3){
		return Rating.COMPLETES_AN_EYE.getValue();
		}else{
			return 0;
		}
	}
	
	
	
	
	//TODO method for detecting eyes
	
	
	
}
