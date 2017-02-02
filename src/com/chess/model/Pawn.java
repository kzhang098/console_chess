package com.chess.model;

import java.util.ArrayList;

import com.chess.service.Chessboard;
import com.chess.service.Piece;
//import com.chess.view.Chess;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class Pawn extends Piece{
	
	private String color;
	private int enpassVal = -1; 
	
	public Pawn(String color) {
		this.color = color;
	}

	@Override
	public ArrayList<int[]> allMoves(int[] origin) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		Piece p;
		int originRow = origin[0], originCol = origin[1];
		int[] move;
		int mod = color.equals("Black") ? 1 : -1;

		if (color.equals("Black") ? origin[0] == 1 : origin[0] == 6) { // a pawn can move two spaces forward on its first turn
			move = new int[] {originRow+(mod*2), originCol};
			p = Chessboard.getPieceAt(move);
			if (p == null) {
				moves.add(move);
			}
		}
		move = new int[] {originRow+mod, originCol};
		if (Chessboard.isValid(move)) {
			p = Chessboard.getPieceAt(move);
			if (p == null)
				moves.add(move);
			move = new int[] {originRow+mod, originCol-1};
			if (Chessboard.isValid(move)) {
				p = Chessboard.getPieceAt(move);
				if (p != null && p.opposes(this)) {
					moves.add(new int[]{originRow+mod, originCol-1});
				}
			}
			move = new int[] {originRow+mod, originCol+1};
			if (Chessboard.isValid(move)) {
				p = Chessboard.getPieceAt(move);
				if (p != null && p.opposes(this)) {
					moves.add(move);
				}				
			}
		}

		return moves;
	}
	
	public int getEnpassVal() {
		return enpassVal;
	}

	public void setEnpassVal(int enpassVal) {
		this.enpassVal = enpassVal;
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
	
	@Override
	public String toString() {
		if (color.equals("Black")) {
			return "bp ";
		} else {
			return "wp ";
		}

	}
	

}
