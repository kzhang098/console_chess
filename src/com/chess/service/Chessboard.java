package com.chess.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.chess.model.Bishop;
import com.chess.model.King;
import com.chess.model.Knight;
import com.chess.model.Pawn;
import com.chess.model.Queen;
import com.chess.model.Rook;

/**
 * 
 * @author Kenneth Zhang and Paul Warner
 *
 */

public class Chessboard {

	private static Piece[][] chessBoard = new Piece[8][8];
	static int turnNum = 0;
	private static String turn;
	boolean checkmate = false;
	private static int drawOfferedOn = -1;
	private static boolean drawAccepted = false;
	private static boolean resigned = false;

	/**
	 * Populate the initial chess board to begin the game. Note that Black
	 * always begins on top and white begins on the bottom.
	 */
	private static void populateBoard() {
		// set up pawns
		for (int col = 0; col < 8; col++) {
			chessBoard[1][col] = new Pawn("Black");
			chessBoard[6][col] = new Pawn("White");
		}

		// set up Knights

		chessBoard[0][1] = new Knight("Black");
		chessBoard[0][6] = new Knight("Black");

		chessBoard[7][1] = new Knight("White");
		chessBoard[7][6] = new Knight("White");

		// set up rooks

		chessBoard[0][0] = new Rook("Black");
		chessBoard[0][7] = new Rook("Black");

		chessBoard[7][0] = new Rook("White");
		chessBoard[7][7] = new Rook("White");

		// set up bishops

		chessBoard[0][2] = new Bishop("Black");
		chessBoard[0][5] = new Bishop("Black");

		chessBoard[7][2] = new Bishop("White");
		chessBoard[7][5] = new Bishop("White");

		// set up queens

		chessBoard[0][3] = new Queen("Black");
		chessBoard[7][3] = new Queen("White");

		// set up kings

		chessBoard[0][4] = new King("Black");
		chessBoard[7][4] = new King("White");
	}

	/**
	 * Outputs the current state of the board to stdout.
	 */
	private static void displayBoard() {
		System.out.println();

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (chessBoard[row][col] != null) {
					System.out.print(chessBoard[row][col]);
				} else {
					if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) { // black
																							// spaces
						System.out.print("## ");
					} else {
						System.out.print("   ");
					}
				}

				if (col == 7) {
					System.out.print(8 - row + "\n");
				}
			}
		}

		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();

	}

	/**
	 * given a particular row and col, determine if pos is a valid index. In
	 * practical terms, figure out of pos is somewhere on our 8x8 grid.
	 * 
	 * @return whether or not pos is on the board
	 */
	public static boolean isValid(int[] pos) {
		return pos[0] <= 7 && pos[0] >= 0 && pos[1] <= 7 && pos[1] >= 0;
	}

	/**
	 * The main game loop. Sets up the board, and runs until the program
	 * terminates.
	 */
	public static void play() {
		populateBoard(); 
		
		turn = "White";
		
		int[][] command;
		
		displayBoard();
		
		System.out.print(turn+"'s move: ");
		
		while(true) {
			command = getInput();
			if (command != null) {
				if (resigned) {
					System.out.println((turn.equals("Black") ? "White" : "Black")+ " wins");
					break;
				}
				if (drawOfferedOn != -1 && drawOfferedOn != turnNum) {
					if (drawAccepted) {
						System.out.println("Draw");
						break; // ends in a draw
					} else {
						drawOfferedOn = -1;
					}
				}
				if (move(command)) {
					displayBoard();
					turnNum++;
					turn = turn.equals("Black") ? "White" : "Black";
					if (inStalemate(turn)) {
						if (inCheck(turn)) { // Checkmate!
							System.out.println("Checkmate\n");
							System.out.println((turnNum % 2 == 0 ? "Black": "White")+" wins");
						} else {
							System.out.println("Stalemate");
						}
						break;
					}
					if (inCheck(turn)) {
						System.out.println("Check");
					}
					System.out.print(turn+"'s move: ");
					continue;
				}
			}
			System.out.print("\nIllegal move, try again ");
			drawOfferedOn = -1;
		}
	}

	/**
	 * Translate an input string into 2d coordinates.
	 * 
	 * @param input
	 *            a string to be converted. This has the format XY, where X is a
	 *            letter and Y is a number.
	 * @return a 2-vector representing our output. If input is invalid, return
	 *         null.
	 */
private static int[] inputConvert(String input) { 
		
		if (input.length() != 2)
			return null;

		int[] output = new int[2];

		char letter = input.charAt(0);
		int colVal = 0;
		char mid = input.charAt(1);
		int rowVal = 8 - Character.getNumericValue(mid);

		switch (letter) {
		case 'a':
			colVal = 0;
			break;

		case 'b':
			colVal = 1;
			break;

		case 'c':
			colVal = 2;
			break;

		case 'd':
			colVal = 3;
			break;

		case 'e':
			colVal = 4;
			break;

		case 'f':
			colVal = 5;
			break;

		case 'g':
			colVal = 6;
			break;

		case 'h':
			colVal = 7;
			break;

		default:
			return null;
		}

		output[0] = rowVal;
		output[1] = colVal;
		
		return output;
	}
	
	/**
	 * Read in a command from the user and translate it into a 2x2 in array of instructions.
	 * @return A 2x2 array with an additional 1 array for promotions, or null if input is not valid
	 */
	private static int[][] getInput() {
		String reply;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String[] strSplit;
		int[] origin;
		int[] dest;
		int[] third;
	
		try {
			reply = br.readLine();
			reply.toLowerCase();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		strSplit = reply.trim().split("( +)"); // Splits the strings
		if (strSplit.length == 1) {
			if (strSplit[0].equals("resign")) {
				resigned = true;
				return new int[][]{};
			} else if (strSplit[0].equals("draw") && drawOfferedOn != -1) {
				drawAccepted = true;
				return new int[][] {};
			} else {
				return null;
			}
		}
		else if (strSplit.length > 1) {
			origin = inputConvert(strSplit[0]);
			dest = inputConvert(strSplit[1]);
			if (strSplit.length == 2) {
				third = new int[] {0};
			}
			else if (strSplit.length == 3) {
				if (strSplit[2].equals("draw?")) {
					drawOfferedOn = turnNum;
					third = new int[] {0};
				} else {
					third = parseLetter(strSplit[2]);
				}
			}
			else if (strSplit.length == 4 && strSplit[3].equals("draw?")) {
				drawOfferedOn = turnNum;
				third = parseLetter(strSplit[2]);
			} else { // illegal move
				third = null;
			}
		
			return origin == null || dest == null || third == null ? null : new int[][] {origin, dest, third};
		}
	
		return null;
	}
	
	/**
	 * Translates a letter into a number that will indicate which pawn promotion is chosen by the player
	 * @param letter This is a string containing the pawn promotion the player wishes to make.
	 * @return An integer array that contains info which is essential to the pawnPromotion method, which will determine which promotion to choose.
	 */
	
	
	private static int[] parseLetter(String letter) {
		if(letter.length() != 1) {
			return null;
		}
		
		int r;
		letter.toUpperCase();
		
		switch(letter.charAt(0)) {
		case 'Q':
			r = 0;
			break;
		case 'B':
			r = 1;
			break;
		case 'N':
			r = 2;
			break;
		case 'R':
			r = 3;
			break;
		default:
			return null;
		}
		return new int[] {r};
	}

	/**
	 * Find a particular piece on the board. If no piece is found, return null.
	 * 
	 * @param pos
	 *            2-vector coordinates
	 * @return The piece at the given index
	 */
	public static Piece getPieceAt(int[] pos) {
		if (!isValid(pos)) { // invalid move
			throw new IllegalArgumentException("Invalid indices: " + pos[0] + ":" + pos[1]);
		}
		return chessBoard[pos[0]][pos[1]];

	}

	/**
	 * Move the piece at origin to dest, including check if this is a valid
	 * move.
	 * 
	 * @param origin
	 *            the starting position of the piece.
	 * @param dest
	 *            the desired end position of the piece.
	 * @return true if the move was completed successfully, false otherwise.
	 */
	private static boolean move(int[][] input) {
		
		int[] origin = input[0];
		int[] dest = input[1];
		
		Piece currentPiece = getPieceAt(origin);
		
		if (currentPiece != null && currentPiece.getColor().equals(turn)) { //checks if the piece can be moved by current player
			if(currentPiece instanceof King) { //checks if the piece is king. 
				if(castle(origin,dest) && !willCheck(turn,origin,dest)) {
					if(dest[1] == 6) { //castle king side
						if(turnNum%2 == 0) {//white castle
							chessBoard[7][6] = getPieceAt(origin);
							chessBoard[7][6].setHasMoved(true);
							dest[1] = 7;
							chessBoard[7][5] = getPieceAt(dest); 
							chessBoard[7][5].setHasMoved(true);
							dest[1] = 6;
						
							chessBoard[7][4] = null;
							chessBoard[7][7] = null;
						} else { //black castle
							chessBoard[0][6] = getPieceAt(origin);
							chessBoard[0][6].setHasMoved(true);
							dest[1] = 7;
							chessBoard[0][5] = getPieceAt(dest); 
							chessBoard[0][5].setHasMoved(true);
							dest[1] = 6;
							
							chessBoard[0][4] = null;
							chessBoard[0][7] = null;
						}
						
						
						return true;
						
					} else { //castle queen side
						if(turnNum%2 == 0) {//white castle
							chessBoard[7][2] = getPieceAt(origin);
							chessBoard[7][2].setHasMoved(true);
							dest[1] = 0; 
							chessBoard[7][3] = getPieceAt(dest);
							chessBoard[7][3].setHasMoved(true);
							dest[1] = 2;
						
							chessBoard[7][0] = null;
							chessBoard[7][4] = null;
						} else {
							chessBoard[0][2] = getPieceAt(origin);
							chessBoard[0][2].setHasMoved(true);
							dest[1] = 0; 
							chessBoard[0][3] = getPieceAt(dest);
							chessBoard[0][3].setHasMoved(true);
							dest[1] = 2;
							
							chessBoard[0][0] = null;
							chessBoard[0][4] = null;
						}
					}
					
					return true; //castling complete
				}	else if(currentPiece.canReach(origin, dest) && !willCheck(turn, origin, dest)) {
						
						chessBoard[dest[0]][dest[1]] = currentPiece;
						chessBoard[origin[0]][origin[1]] = null;
						chessBoard[dest[0]][dest[1]].setHasMoved(true);
						return true;	
				}
			} else if(isPromotion(turn, origin, dest) && !willCheck(turn,origin,dest) && currentPiece.canReach(origin,dest)) {
				pawnPromotion(origin, dest, input[2]);
				chessBoard[dest[0]][dest[1]].setHasMoved(true);
				return true;
			} else if (canEnpassant(origin,dest) && !willCheck(turn,origin,dest)) { // I ADDED THIS
				if(turn.equals("White")) {
					chessBoard[dest[0]][dest[1]] = getPieceAt(origin);
					chessBoard[dest[0] + 1][dest[1]] = null;
					chessBoard[origin[0]][origin[1]] = null;
					chessBoard[dest[0]][dest[1]].setHasMoved(true);
					return true;
				} else {
					chessBoard[dest[0]][dest[1]] = getPieceAt(origin);
					chessBoard[dest[0] - 1][dest[1]] = null;
					chessBoard[origin[0]][origin[1]] = null;
					chessBoard[dest[0]][dest[1]].setHasMoved(true);
					return true;
				}
			} else if (currentPiece.canReach(origin, dest) && !willCheck(turn, origin, dest)) {
					
				chessBoard[dest[0]][dest[1]] = currentPiece;
				chessBoard[origin[0]][origin[1]] = null;
				
				if(currentPiece instanceof Pawn) {
					chessBoard[dest[0]][dest[1]].setEnpassVal(turnNum + 1);
				}
				chessBoard[dest[0]][dest[1]].setHasMoved(true);
				return true;
			}	
				
		}
		return false;
	}

	/**
	 * check if a particular movement will be considered a promotion
	 * @param origin the starting position
	 * @param dest the ending position
	 * @return Whether or not the piece will be promoted
	 */
	private static boolean isPromotion(String color, int[] origin, int[] dest) {		
		return getPieceAt(origin) instanceof Pawn && color == "Black" ? dest[0] == 7 : dest[0] == 0;
	}

	/**
	 * Check if the given color is currently in a checked state.
	 * 
	 * @param color
	 *            The color who we are examining.
	 * @return true if the given color is in check, and false otherwise.
	 */
	public static boolean inCheck(String color) {
		int[] king = getIndexOf(findKing(color));
		Piece p;
		for (int i = 0; i < chessBoard.length; ++i) {
			for (int j = 0; j < chessBoard[i].length; ++j) {
				p = chessBoard[i][j];
				if (p != null && p.getColor() != color && p.canReach(new int[] {i, j}, king)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Given a particular movement of a piece, decide whether that move will put the given color
	 * in check.
	 * @param color the color who we are check if in check
	 * @param origin Starting position of the piece to be moved.
	 * @param dest ending position of the piece to be moved. 
	 * @return
	 */
	public static boolean willCheck(String color, int[] origin, int[] dest) {
		Piece popper = chessBoard[dest[0]][dest[1]];
		chessBoard[dest[0]][dest[1]] = chessBoard[origin[0]][origin[1]];
		chessBoard[origin[0]][origin[1]] = null;
		boolean check = inCheck(color);
		chessBoard[origin[0]][origin[1]] = chessBoard[dest[0]][dest[1]];
		chessBoard[dest[0]][dest[1]] = popper;
		return check;
	}
	
	/**
	 * Confirm whether or not a player is in stalemate. A player is in stalemate if he has no
	 * valid moves that will not put him in check
	 * @param color the color to be checked
	 * @return Whether the given color is in stalemate
	 */
	private static boolean inStalemate(String color) {
		int[] pos;
		ArrayList<int[]> moves;
		Piece popper;
		for (int i = 0; i < chessBoard.length; ++i) {
			for (Piece p : chessBoard[i]) {
				if (p != null && p.getColor().equals(color)) {
					pos = getIndexOf(p);
					moves = p.allMoves(pos);
					for (int[] m : moves) {
						popper = chessBoard[m[0]][m[1]];
						chessBoard[m[0]][m[1]] = p;
						chessBoard[pos[0]][pos[1]] = null;
						if (!inCheck(color)) {
							chessBoard[pos[0]][pos[1]] = p;
							chessBoard[m[0]][m[1]] = popper;
							return false;
						} else {
							chessBoard[pos[0]][pos[1]] = p;
							chessBoard[m[0]][m[1]] = popper;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Given a particular color find that color's king somewhere on the board
	 * 
	 * @param color
	 *            The color whose king we want to find
	 * @return The color's respective king.
	 */
	private static King findKing(String color) {
		for (Piece[] a : chessBoard) {
			for (Piece p : a) {
				if (p instanceof King && p.getColor().equals(color)) {
					return (King) p;
				}
			}
		}
		return null;
	}

	
	/**
	 * This method checks if the current player is able to castle his or her king 
	 * 
	 * @param origin
	 *    The origin of the king 
	 * @param dest
	 * 	  The destination of the king. Will indicate whether the player wants to castle king or queen side.
	 * @return A boolean value that will indicate whether the user is able to castle. 
	 */
	
	private static boolean castle(int [] origin, int [] dest) {
		
		int oRow = origin[0];
		int oCol = origin[1];
		int dRow = dest[0];
		int dCol = dest[1];
		
		int index = 4;
		int destCol = dest[1];
		
		if(turnNum%2 == 0) {//white's turn 
		
		if(origin[0] == 7 && origin[1] == 4 && ((dest[0] == 7 && dest[1] == 6) || ( dest[0] == 7 && dest[1] == 2))) {
			if(destCol == 6) {
				dest[1] = 7;
				if(getPieceAt(origin) instanceof King && getPieceAt(dest) instanceof Rook) {
					King king = (King)getPieceAt(origin); 
					Rook rook = (Rook)getPieceAt(dest); 
				
					dest[1] = 6;
				
					if(!king.isHasMoved() && !rook.isHasMoved()) { //king and rook in question have not moved
						index++;
						while(index != 7) {
							dest[1] = index;
							if(getPieceAt(dest) != null) {
								origin[0] = oRow;
								origin[1] = oCol;
								dest[0] = dRow;
								dest[1] = dCol;
								return false;
							} 
							index++;
						}
						origin[0] = oRow;
						origin[1] = oCol;
						dest[0] = dRow;
						dest[1] = dCol;
						return true;
					} else {
						origin[0] = oRow;
						origin[1] = oCol;
						dest[0] = dRow;
						dest[1] = dCol;
						return false; 
					}
				}	
			
			} else if(destCol == 2) {
				
				dest[1] = 0;
				
				if(getPieceAt(origin) instanceof King && getPieceAt(dest) instanceof Rook) {
					
					King king = (King)getPieceAt(origin); 
					Rook rook = (Rook)getPieceAt(dest); 
					
					dest[1] = 2;
					
					if(!king.isHasMoved() && !rook.isHasMoved()) { //king and rook in question have not moved	
							index--; 
							while(index != 0) {
								dest[1] = index;
								if(getPieceAt(dest) != null) {
									origin[0] = oRow;
									origin[1] = oCol;
									dest[0] = dRow;
									dest[1] = dCol;
									return false;
								}
								index--;
							}
							
							origin[0] = oRow;
							origin[1] = oCol;
							dest[0] = dRow;
							dest[1] = dCol;
							
							return true;
					} else {
						
						origin[0] = oRow;
						origin[1] = oCol;
						dest[0] = dRow;
						dest[1] = dCol;
						
						return false; 
					}
				}	
			}
		}
		} else { //black's turn 
			if(origin[0] == 0 && origin[1] == 4 && ((dest[0] == 0 && dest[1] == 6) || ( dest[0] == 0 && dest[1] == 2))) {
				if(destCol == 6) {
					dest[1] = 7;
					if(getPieceAt(origin) instanceof King && getPieceAt(dest) instanceof Rook) {
						King king = (King)getPieceAt(origin); 
						Rook rook = (Rook)getPieceAt(dest); 
					
						dest[1] = 6;
					
						if(!king.isHasMoved() && !rook.isHasMoved()) { //king and rook in question have not moved
							index++;
							while(index != 7) {
								dest[1] = index;
								if(getPieceAt(dest) != null) {
									
									origin[0] = oRow;
									origin[1] = oCol;
									dest[0] = dRow;
									dest[1] = dCol;
									
									return false;
								} 
								index++;
							}
							
							origin[0] = oRow;
							origin[1] = oCol;
							dest[0] = dRow;
							dest[1] = dCol;
							
							return true;
						} else {
							
							origin[0] = oRow;
							origin[1] = oCol;
							dest[0] = dRow;
							dest[1] = dCol;
							
							return false; 
						}
					}	
				
				} else if(destCol == 2) {
					
					dest[1] = 0;
					
					if(getPieceAt(origin) instanceof King && getPieceAt(dest) instanceof Rook) {
						
						King king = (King)getPieceAt(origin); 
						Rook rook = (Rook)getPieceAt(dest); 
						
						dest[1] = 2;
						
						if(!king.isHasMoved() && !rook.isHasMoved()) { //king and rook in question have not moved	
								index--; 
								while(index != 0) {
									dest[1] = index;
									if(getPieceAt(dest) != null) {
										
										origin[0] = oRow;
										origin[1] = oCol;
										dest[0] = dRow;
										dest[1] = dCol;
										
										return false;
									}
									index--;
								}
								
								origin[0] = oRow;
								origin[1] = oCol;
								dest[0] = dRow;
								dest[1] = dCol;
								
								return true;
						} else {
							
							origin[0] = oRow;
							origin[1] = oCol;
							dest[0] = dRow;
							dest[1] = dCol;
							
							return false; 
						}
					}	
				}
			}
			
		}

		origin[0] = oRow;
		origin[1] = oCol;
		dest[0] = dRow;
		dest[1] = dCol;
		
		return false;
	}
	
	/**
	 * Promote the pawn by moving it from origin to dest
	 * @param origin the starting position of the pawn
	 * @param dest the position of the new piece
	 */
	private static void pawnPromotion(int [] origin, int [] dest, int[] newpiece) {
		
		chessBoard[origin[0]][origin[1]] = null;
		switch (newpiece[0]) {
		case 0:
			chessBoard[dest[0]][dest[1]] = new Queen(turn);
			break;
		case 1:
			chessBoard[dest[0]][dest[1]] = new Bishop(turn);
			break;
		case 2:
			chessBoard[dest[0]][dest[1]] = new Knight(turn);
			break;
		case 3:
			chessBoard[dest[0]][dest[1]] = new Rook(turn);
			break;
		}

	}
		
	

	/**
	 * Get the coordinates of a given piece
	 * 
	 * @param t
	 *            the piece to examine.
	 * @return a 2-vector containing the coordinates of p.
	 */
	private static int[] getIndexOf(Piece t) {
		for (int i = 0; i < chessBoard.length; ++i) {
			for (int j = 0; j < chessBoard[i].length; ++j) {
				if (t == chessBoard[i][j]) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if the piece located at origin is going to and is able to enpassant another pawn. 
	 * @param origin An integer array containing the coordinates of the piece in question
	 * @param dest The proposed destination of the piece that is going to move
	 * @return A boolean value that indicates whether the piece is a pawn and whether the destination is an enpassant move 
	 */
	
	private static boolean canEnpassant(int [] origin, int [] dest) {
		
		int oRow = origin[0];
		int oCol = origin[1];
		int dRow = dest[0];
		int dCol = dest[1];
		
		int enemyPawnLoc = 0; 
		
		if(turn.equals("White")) {
			
			enemyPawnLoc = dest[0] + 1;
			dest[0] = enemyPawnLoc;
			
			if(origin[0] == 3) {
				if(getPieceAt(dest) instanceof Pawn && getPieceAt(origin).opposes(getPieceAt(dest))) { //checks adjacent space
					if(turnNum == getPieceAt(dest).getEnpassVal()) {
						origin[0] = oRow;
						origin[1] = oCol;
						dest[0] = dRow;
						dest[1] = dCol;
						return true;
					}
				}
			}//white row 3 
		
		origin[0] = oRow;
		origin[1] = oCol;
		dest[0] = dRow;
		dest[1] = dCol;
			
		return false; 
		
		} else { //
			
			enemyPawnLoc = dest[0] - 1; //here
			dest[0] = enemyPawnLoc;
			
			if(origin[0] == 4) {
				if(getPieceAt(dest) instanceof Pawn && getPieceAt(origin).opposes(getPieceAt(dest))) { //checks adjacent space
					Pawn enemyPawn = (Pawn)getPieceAt(dest);
					if(turnNum == enemyPawn.getEnpassVal()) {
						origin[0] = oRow;
						origin[1] = oCol;
						dest[0] = dRow;
						dest[1] = dCol;
						return true;
					}
				}
			}
			//black row 4
		}
		origin[0] = oRow;
		origin[1] = oCol;
		dest[0] = dRow;
		dest[1] = dCol;
		return false; 
	}

}
