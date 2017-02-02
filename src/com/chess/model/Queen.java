//Changed queen

package com.chess.model;

import java.util.ArrayList;

//import com.chess.service.Chessboard;
import com.chess.service.Piece;
//import com.chess.view.Chess;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class Queen extends Piece{
	
	public Queen(String color) {
		this.color = color;
	}
	
	public ArrayList <int[]> allMoves(int[] origin) {
		ArrayList <int[]> moves = new ArrayList<int[]> ();
		moves.addAll(walkDirection(origin, 0, 1));
		moves.addAll(walkDirection(origin, 1, 0));
		moves.addAll(walkDirection(origin, -1, 0));
		moves.addAll(walkDirection(origin, 0, -1));
		moves.addAll(walkDirection(origin, 1, 1));
		moves.addAll(walkDirection(origin, -1, -1));
		moves.addAll(walkDirection(origin, -1, 1));
		moves.addAll(walkDirection(origin, 1, -1));
		
		return moves;
	}
	
	public void move(int originCol, int originRow, int destCol, int destRow){
		
	}

	public boolean isHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	public String getColor(){
		return color; 
	}
	
	public String toString() {
		if(this.color.equals("White")) {
			return "wQ ";
		} else {
			return "bQ ";
		}
	}
}
