package com.chess.service;

import java.util.ArrayList;

import com.chess.service.Chessboard;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public abstract class Piece {
protected String color;
protected boolean hasMoved = false;
protected int enpassVal = -1; 

	
	/**
	 * Given a particular row and column check if a piece reach that space
	 * @param origin starting position of the piece
	 * @param dest ending position of the piece
	 * @return: [destCol, destRow] is a valid space
	 */
	public boolean canReach(int[] origin, int[] dest) {
		ArrayList<int[]> moves = this.allMoves(origin);
		for (int[] i: moves) {
			if (i[0] == dest[0] && i[1] == dest[1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given a particular starting point, get all possible moves that come from moving 
	 * in that direction until you either reach the end of the board or hit another piece
	 * @param move the position we start from.
	 * @param rowmod What we change our row by each move.
	 * @param colmod What we change our column by each move.
	 * @return All possible spaces the piece can reach in that direction.
	 */
	protected ArrayList<int[]> walkDirection(int[] move, int rowmod, int colmod) {
		Piece p;
		ArrayList<int[]> moves = new ArrayList<int[]>();
		move = move.clone();
		move[0] += rowmod;
		move[1] += colmod;
		while (Chessboard.isValid(move)) {
			p = Chessboard.getPieceAt(move);
			if (p != null) {
				if (p.opposes(this)) {
					moves.add(move.clone());
				} 
				break;
			} else {
				moves.add(move.clone());
			}
			move[0] += rowmod;
			move[1] += colmod;
		}
		return moves;
	}
	
	/**
	 * Give a particular board and position, return an ArrayList of all spaces that
	 * would be valid moves
	 * @param origin starting position of the piece
	 * @return list of all valid moves
	 */
	public abstract ArrayList<int[]> allMoves(int[] origin);

	/**
	 * Return the color of a particular space
	 * @return String representing the color of the space
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * Shows us whether or not the piece has moved
	 * @return boolean value that indicates whether piece has moved or not
	 */
	
	public boolean isHasMoved() {
		return hasMoved;
	}

	/**
	 * Sets whether or not a piece has moved
	 * @param hasMoved boolean value that indicates whether piece has moved
	 */
	
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	/**
	 * returns true if these pieces are on opposing sides or false if not
	 * @param p piece to be compared
	 * @return whether or not these pieces are on opposing sides
	 */
	public boolean opposes(Piece p) {
		return !p.getColor().equals(this.getColor());
	}
	
	/**
	 * Retrieves the enpassVal that will help indicate whether the Pawn can be taken by enpassant. Not relevant for other pieces
	 * @return integer value stored in enpassVal.
	 */
	
	public int getEnpassVal() {
		return enpassVal;
	}

	/**
	 * Sets an enpassVal to the Pawn. (Will not be set for non-Pawn instances). 
	 * @param enpassVal An integer value that is the current turn number + 1. 
	 */
	
	public void setEnpassVal(int enpassVal) {
		this.enpassVal = enpassVal;
	}
}
