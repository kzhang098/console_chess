package com.chess.model;

import java.util.ArrayList;

import com.chess.service.Piece;
import com.chess.service.Chessboard;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class King extends Piece{
	
	public King(String color) {
		this.color = color; 
	}
	
	@Override
	public ArrayList<int[]> allMoves(int[] origin) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		moves.addAll(walkDirection(origin, 0, 1));
		moves.addAll(walkDirection(origin, 1, 0));
		moves.addAll(walkDirection(origin, 1, 1));
		moves.addAll(walkDirection(origin, -1, 0));
		moves.addAll(walkDirection(origin, 0, -1));
		moves.addAll(walkDirection(origin, -1, -1));
		moves.addAll(walkDirection(origin, 1, -1));
		moves.addAll(walkDirection(origin, -1, 1));
		return moves;
	}
	
	protected ArrayList<int[]> walkDirection(int[] move, int rowmod, int colmod) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		int[] dest = move.clone();
		dest[0] += rowmod;
		dest[1] += colmod;
		Piece popper;
		if (Chessboard.isValid(dest)) {
			popper = Chessboard.getPieceAt(dest);
			if (popper == null || popper.opposes(this)) {
				moves.add(dest);
			}
		}
		return moves;
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
			return "wK ";
		} else {
			return "bK ";
		}
	}
}
