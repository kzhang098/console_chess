package com.chess.model;

import java.util.ArrayList;

import com.chess.service.Piece;
import com.chess.service.Chessboard;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class Knight extends Piece{
	
	private String color;
	
	public Knight(String color) {
		this.color = color;
	}

	@Override
	public ArrayList<int[]> allMoves(int[] origin) {
		ArrayList<int[]> moves = new ArrayList<int []>();
		moves.addAll(walkDirection(origin, 0, 2));
		moves.addAll(walkDirection(origin, 2, 0));
		moves.addAll(walkDirection(origin, -2, 0));
		moves.addAll(walkDirection(origin, 0, -2));
		
		return moves;
	}
	
	protected ArrayList<int[]> walkDirection(int[] move, int rowmod, int colmod) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		Piece p;
		move = move.clone(); // don't touch original object
		move[0] += rowmod;
		move[1] += colmod;
		if (Chessboard.isValid(move)) {
			if (rowmod == 0) { // moving in the column direction
				move[0]++;
				if (Chessboard.isValid(move)) {
					p = Chessboard.getPieceAt(move);
					if (p == null || p.opposes(this)) {
						moves.add(move.clone());
					}
				}
				move[0] -= 2;
				if (Chessboard.isValid(move)) {
					p = Chessboard.getPieceAt(move);
					if (p == null || p.opposes(this)) {
						moves.add(move.clone());
					}
				}
			} else { // moving left to right
				move[1]++;
				if (Chessboard.isValid(move)) {
					p = Chessboard.getPieceAt(move);
					if (p == null || p.opposes(this)) {
						moves.add(move.clone());
					}
				}
				move[1] -= 2;
				if (Chessboard.isValid(move)) {
					p = Chessboard.getPieceAt(move);
					if (p == null || p.opposes(this)) {
						moves.add(move.clone());
					}
				}
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
	
	public String getColor() {
		return color;
	}



	@Override
	public String toString() {
		if (color.equals("Black")) {
			return "bN ";
		} else {
			return "wN ";
		}
	}
	
}

