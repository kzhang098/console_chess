package com.chess.model;

import java.util.ArrayList;

import com.chess.service.Piece;
//import com.chess.service.Chessboard;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class Bishop extends Piece{
	

	public Bishop(String color) {
		this.color = color;
	}

	@Override
	public ArrayList<int[]> allMoves(int[] origin) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		moves.addAll(walkDirection(origin, 1, 1));
		moves.addAll(walkDirection(origin, 1, -1));
		moves.addAll(walkDirection(origin, -1, 1));
		moves.addAll(walkDirection(origin, -1, -1));		

		return moves;
	}
	
	public boolean isHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	public String getColor() {
		return color;
	}
	
	public String toString() {
		if (color.equals("Black")){
			return "bB ";
		} else {
			return "wB ";
		}
	}

}
