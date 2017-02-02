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

public class Rook extends Piece{
	
	public Rook(String color) {
		this.color = color;
	}

	@Override
	public ArrayList<int[]> allMoves(int[] origin) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		moves.addAll(walkDirection(origin, 0, 1));
		moves.addAll(walkDirection(origin, 1, 0));
		moves.addAll(walkDirection(origin, -1, 0));
		moves.addAll(walkDirection(origin, 0, -1));
		
		return moves;
//		int originRow = origin[0], originCol = origin[1];
//		int row, col;
//		Piece p;
//		row = originRow+1;
//		col = originCol;
//		while (Chessboard.isValid(row, col)) {
//			p = Chessboard.getPieceAt(row, col);
//			if (p != null) {
//				if (p.opposes(this)) {
//					moves.add(new int[] {row, col});
//				} 
//				break;
//			} else {
//				moves.add(new int[] {row, col});
//			}
//			row++;
//		}
//		row = originRow-1;
//		while (Chessboard.isValid(row, col)) {
//			p = Chessboard.getPieceAt(row, col);
//			if (p != null) {
//				if (p.opposes(this)) {
//					moves.add(new int[] {row, col});
//				} 
//				break;
//			} else {
//				moves.add(new int[]{row, col});
//			}
//			row--;
//		}
//		row = originRow;
//		col = originCol+1;
//		while (Chessboard.isValid(row, col)) {
//			p = Chessboard.getPieceAt(row, col);
//			if (p != null) {
//				if (p.opposes(this)) {
//					moves.add(new int[]{row, col});
//				}
//				break;
//			} else {
//				moves.add(new int[]{row, col});
//			}
//			col++;
//		}
//		col = originCol-1;
//		while (Chessboard.isValid(row, col)) {
//			p = Chessboard.getPieceAt(row, col);
//			if (p != null) {
//				if (p.opposes(this)) {
//					moves.add(new int[]{row, col});
//				}
//				break;
//			} else {
//				moves.add(new int[]{row, col});
//			}
//			col--;
//		}
//		return moves;
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
		if (color.equals("Black")) {
			return "bR ";
		} else {
			return "wR ";
		}
	}
	
}
