package tictactoepac;
import java.util.Scanner;
import java.util.Random;

class GameCode {
	
	// Instance variables
	private char [][] grid = new char[3][3];
	
	private int row;
	private int col;
	private int airow;
	private int aicol;
	private int bestRow;
	private int bestCol;
	private int winRow;
	private int winCol;
	private int moves;
	private int choice;
	private int winCount = 0;
	private int drawCount = 0;

	private char playerChar;
	private char AIChar;
	
	private String winType;
	private String winSpec;
	
	private boolean wentFirst;
	
	private Scanner input = new Scanner(System.in);
	private Random ran = new Random();
	
	/* Constructor to create player object
	 * 
	 * @param player object name
	 */
	public GameCode(String player) {
		System.out.println("\n[SYS] Welcome, " + player + ", to:\n");
		System.out.println(" *** TIC TAC TOE ***");
	}
	
	/* Starts the game with a 3x3 grid
	 * 
	 * Populate grid with dashes
	 * Call methods to begin user input
	 */
	public void startGame() {	
		// Populates the grid with '-' characters
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				grid[row][col] = '-';
			}
		}
		// Resets all instance variables
		row = 0; col = 0; airow = 0; aicol = 0; bestRow = 0; bestCol = 0; winRow = 0; winCol = 0; moves = 0; choice = 0;
		winType = "";
		winSpec = "";
		printGrid();
		// Begins the game with player input
		promptGame();	
	}
	
	/* Prints the current grid
	 * 
	 * Presents in a rectangle display
	 * Called very often
	 * Doesn't call other methods
	 */
	public void printGrid() {
		System.out.println("");
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[row].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println("");
	}
	
	/* Decides whether or not the player is choosing to move first
	 * 
	 * Sets up player going first
	 * Sets up AI going first
	 * Repeats if invalid
	 */
	public void promptGame() {
		// Set up scanner
		Scanner inputPrompt = new Scanner(System.in);
		System.out.print("[SYS] Do you want to go first (y/n)? ");
		String yn = inputPrompt.next();
		// Sets up the game if player is going first
		if(yn.equalsIgnoreCase("yes") || yn.equalsIgnoreCase("y")) {
			wentFirst = true;
			playerChar = 'X';
			AIChar = 'O';
			// Call player move method
			makeMove();
		// Sets up the game if AI is going first
		} else if(yn.equalsIgnoreCase("no") || yn.equalsIgnoreCase("n")) {
			wentFirst = false;
			playerChar = 'O';
			AIChar = 'X';
			// Call AI move method
			checkMoves(moves);
		// Repeat if input is invalid
		} else {
			System.out.println("[SYS] Cannot read input. Please answer yes or no.");
			promptGame();
		}
		inputPrompt.close();	
	}
	
	/* Player makes a move
	 * 
	 * Places the player's piece
	 * Repeats if invalid
	 * Iterates moves
	 * Calls AI move method
	 */
	public void makeMove() {
		while(true) {
			// Sets the player row
			System.out.print("Pick a row (1-3): ");
			int row = input.nextInt();
			if(row == 1 || row == 2 || row == 3) {
				// this reference because of variable naming problems
				// row -1 because of array index
				this.row = row-1;
				break;
			// Repeats if invalid
			} else {
				System.out.println("[SYS] Must be a number from 1-3.");
			} 
		}
		
		while(true) {
			// Sets the player column
			System.out.print("Pick a column (1-3): ");
			int col = input.nextInt();
			if(col == 1 || col == 2 || col == 3) {
				// this reference because of variable naming problems
				// col -1 because of array index
				this.col = col-1;
				break;
			// Repeats if invalid
			} else {
				System.out.println("[SYS] Must be a number from 1-3.");
			}
		}
		// Places the piece if possible
		if(grid[this.row][this.col] == '-') {
			grid[this.row][this.col] = playerChar; 
		// Repeats if not possible
		} else {
			System.out.println("[SYS] Sorry, there's already a piece there. Try again.");
			makeMove();
		}
		// Iterates moves to keep track, then calls AI move method
		moves++;
		printGrid();
		checkMoves(moves);
		
	}
	
	/* Checks the amount of moves made
	 * 
	 * Calls the appropriate method depending on the number of moves
	 * Prints the grid
	 * Calls the player move method
	 * @param moves made
	 */
	public void checkMoves(int moves) {
		// Hard coded to take middle if moving first
		if(moves == 0) {
			// AI calls placePiece to place the piece and print position
			System.out.println(placePiece(1, 1));
			printGrid();
			makeMove();
		// If AI is guaranteed to win, use this method
		} else if (winType.equals("winCon")) {
			// winSpec passes on to call the right if statement
			winCon(winSpec);
			printGrid();
			makeMove();
		// If AI is now trying to force a draw, use this method
		} else if (winType.equals("blockDraw")) {
			// winSpec passes on to call the right if statement
			blockDraw(winSpec);
			printGrid();
			makeMove();
		} else if (moves == 1) {
			moveFirst();
			printGrid();
			makeMove();
		} else if (moves == 2) {
			moveSecond();
			printGrid();
			makeMove();
		}
	}
	
	/* Chooses the AI's first move
	 * 
	 * AI picks the (random) best option
	 * Changes depending on grid locations
	 * See note pad for all possibilities
	 */
	public void moveFirst() {
		// If the AI went first (AI will have middle)
		if(wentFirst == false) {
			/* If player took an outside middle spot
			 * winType = winCon because AI will win
			 * winSpec = midTrap, see text pad
			 */
			if(grid[0][1] == playerChar || grid[1][0] == playerChar || grid[1][2] == playerChar || grid[2][1] == playerChar) {
				winType = "winCon";
				winSpec = "midTrap";
				winCon(winSpec);
			}
			/* If player took a corner
			 * AI takes opposite corner
			 */
			else {
				if(grid[0][0] == playerChar) {
					airow = 2;
					aicol = 2;
				} else if(grid[0][2] == playerChar) {
					airow = 2;
					aicol = 0;
				} else if(grid[2][2] == playerChar) {
					airow = 0;
					aicol = 0;
				} else if(grid[2][0] == playerChar) {
					airow = 0;
					aicol = 2;
				}
				// AI calls placePiece to place the piece and print position
				System.out.println(placePiece(airow, aicol));
			}
		}
		// If player went first
		else if(wentFirst == true) {
			/* If player has a piece in the middle
			 * Select one of the corners at random
			 */
			// [mustPick] to avoid loss
			if(grid[1][1] == playerChar) {
				choice = ran.nextInt(4);
				if(choice == 0) {
					// AI calls placePiece to place the piece and print position
					System.out.println(placePiece(0, 0));
				} else if(choice == 1) {
					// AI calls placePiece to place the piece and print position
					System.out.println(placePiece(0, 2));
				} else if(choice == 2) {
					// AI calls placePiece to place the piece and print position
					System.out.println(placePiece(2, 0));
				} else if(choice == 3) {
					// AI calls placePiece to place the piece and print position
					System.out.println(placePiece(2, 2));
				}
			}
			
			/* If player didn't take the middle spot
			 * Select Middle Spot
			 */
			else {
				// AI calls placePiece to place the piece and print position
				System.out.println(placePiece(1, 1));
			}
		}
	}
	
	/* Chooses the AI's second move
	 * 
	 * AI picks the (random) best option
	 * Changes depending on grid locations
	 */
	public void moveSecond() {
		// If the AI went first (AI will have middle)
		if(wentFirst == false) {
			// If the player took a corner, AI wants to draw
			if((grid[0][2] == playerChar || grid[2][0] == playerChar) && (grid[2][2] == playerChar || grid[0][0] == playerChar)) {
				winType = "blockDraw";
				winSpec = "firstDraw";
				blockDraw(winSpec);
			// If the player took a side, AI will win
			} else {
				winType = "winCon";
				winSpec = "cornerTrap";
				winCon(winSpec);
			} 
		// If the player went first
		} else if (wentFirst == true) {
			// If the player put their first piece in the middle (AI will have corner)
			if(grid[1][1] == playerChar) {
				// First checks top left corner possibilities
				if(grid[0][0] == AIChar) {
					// If player picked adjacent corner, AI wants to draw
					if(grid[0][2] == playerChar || grid[2][0] == playerChar) {
						winType = "blockDraw";
						winSpec = "bestCase";
						blockDraw(winSpec);
					}
					// If player picked opposite side, AI wants to draw
					else if(grid[1][2] == playerChar || grid[2][1] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					} 
					// If player picked opposite corner, AI must pick between two choices
					else if(grid[2][2] == playerChar) {
						choice = ran.nextInt(2);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						} else if(choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					// If player picked adjacent side, AI blocks 
					// Calls possible win because one possibility leads to a win (player picks corner)
					// All other possibilities lead to best case, so programming is more efficient to call
					} else {
						winType = "blockDraw";
						winSpec = "possibleWin";
						blockDraw(winSpec);
					}
				}
				// Then checks top right corner possibilities
				else if(grid[0][2] == AIChar) {
					// If player picked adjacent corner, AI wants to draw
					if(grid[0][0] == playerChar || grid[2][2] == playerChar) {
						winType = "blockDraw";
						winSpec = "bestCase";
						blockDraw(winSpec);
					}
					// If player picked opposite side, AI wants to draw
					else if(grid[1][0] == playerChar || grid[2][1] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					} 
					// If player picked opposite corner, AI must pick between two choices
					else if(grid[2][0] == playerChar) {
						choice = ran.nextInt(2);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if(choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					// If player picked adjacent side, AI blocks (only possibility left)
					// Calls possible win because one possibility leads to a win (player picks corner)
					// All other possibilities lead to best case, so programming is more efficient to call
					} else {
						winType = "blockDraw";
						winSpec = "possibleWin";
						blockDraw(winSpec);
					}
				}
				// Then checks bottom left corner possibilities
				else if(grid[2][0] == AIChar) {
					// If player picked adjacent corner, AI wants to draw
					if(grid[0][0] == playerChar || grid[2][2] == playerChar) {
						winType = "blockDraw";
						winSpec = "bestCase";
						blockDraw(winSpec);
					}
					// If player picked opposite side, AI wants to draw
					else if(grid[0][1] == playerChar || grid[1][2] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					} 
					// If player picked opposite corner, AI must pick between two choices
					else if(grid[0][2] == playerChar) {
						choice = ran.nextInt(2);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if(choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					// If player picked adjacent side, AI blocks (only possibility left)
					// Calls possible win because one possibility leads to a win (player picks corner)
					// All other possibilities lead to best case, so programming is more efficient to call
					} else {
						winType = "blockDraw";
						winSpec = "possibleWin";
						blockDraw(winSpec);
					}
				}
				// Then checks bottom right corner possibilities
				else if(grid[2][2] == AIChar) {
					// If player picked adjacent corner, AI wants to draw
					if(grid[0][2] == playerChar || grid[2][0] == playerChar) {
						winType = "blockDraw";
						winSpec = "bestCase";
						blockDraw(winSpec);
					}
					// If player picked opposite side, AI wants to draw
					else if(grid[0][1] == playerChar || grid[1][0] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					} 
					// If player picked opposite corner, AI must pick between two choices
					else if(grid[0][0] == playerChar) {
						choice = ran.nextInt(2);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						} else if(choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					// If player picked adjacent side, AI blocks (only possibility left)
					// Calls possible win because one possibility leads to a win (player picks corner)
					// All other possibilities lead to best case, so programming is more efficient to call
					} else {
						winType = "blockDraw";
						winSpec = "possibleWin";
						blockDraw(winSpec);
					}
				}
			// If player didn't choose the middle, then we check if they chose a corner (first or second turn, doesn't matter)
			} else if (grid[0][0] == playerChar || grid[0][2] == playerChar || grid[2][0] == playerChar || grid[2][2] == playerChar) {
				// First check top left possibilities
				if(grid[0][0] == playerChar) {
					// Checks is a block draw is possible
					if(grid[0][1] == playerChar || grid[0][2] == playerChar || grid[1][0] == playerChar || grid[2][0] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					}
					// If player picked adjacent side, AI must pick the opposite corner to avoid potential loss
					else if(grid[2][1] == playerChar || grid[1][2] == playerChar) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 2));
						// Calls possible win because one possibility leads to a win (player picks corner)
						// All other possibilities lead to best case, so programming is more efficient to call
						winType = "blockDraw";
						winSpec = "possibleWin";
					}
					// If player picked opposite side, AI must pick a random side to draw
					else if(grid[2][2] == playerChar) {
						choice = ran.nextInt(4);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 1));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 2));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 1));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					}
				}
				// Then check top right possibilities
				else if(grid[0][2] == playerChar) {
					// Checks is a block draw is possible
					if(grid[0][1] == playerChar || grid[0][0] == playerChar || grid[1][2] == playerChar || grid[2][2] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					}
					// If player picked adjacent side, AI must pick the opposite corner to avoid potential loss
					else if(grid[2][1] == playerChar || grid[1][0] == playerChar) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 0));
						// Calls possible win because one possibility leads to a win (player picks corner)
						// All other possibilities lead to best case, so programming is more efficient to call
						winType = "blockDraw";
						winSpec = "possibleWin";
					}
					// If player picked opposite side, AI must pick a random side to draw
					else if(grid[2][0] == playerChar) {
						choice = ran.nextInt(4);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 1));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 2));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 1));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					}
				} 
				// Then check bottom left possibilities
				else if(grid[2][0] == playerChar) {
					// Checks is a block draw is possible
					if(grid[2][1] == playerChar || grid[2][2] == playerChar || grid[1][0] == playerChar || grid[0][0] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					}
					// If player picked adjacent side, AI must pick the opposite corner to avoid potential loss
					else if(grid[0][1] == playerChar || grid[1][2] == playerChar) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 2));
						// Calls possible win because one possibility leads to a win (player picks corner)
						// All other possibilities lead to best case, so programming is more efficient to call
						winType = "blockDraw";
						winSpec = "possibleWin";
					}
					// If player picked opposite side, AI must pick a random side to draw
					else if(grid[0][2] == playerChar) {
						choice = ran.nextInt(4);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 1));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 2));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 1));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					}
					
				} 
				// Then check bottom right possibilities
				else if(grid[2][2] == playerChar) {
					// Checks is a block draw is possible
					if(grid[2][1] == playerChar || grid[2][0] == playerChar || grid[1][2] == playerChar || grid[0][2] == playerChar) {
						winType = "blockDraw";
						winSpec = "trueBlock";
						blockDraw(winSpec);
					}
					// If player picked adjacent side, AI must pick the opposite corner to avoid potential loss
					else if(grid[0][1] == playerChar || grid[1][0] == playerChar) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 0));
						// Calls possible win because one possibility leads to a win (player picks corner)
						// All other possibilities lead to best case, so programming is more efficient to call
						winType = "blockDraw";
						winSpec = "possibleWin";
					}
					// If player picked opposite side, AI must pick a random side to draw
					else if(grid[0][0] == playerChar) {
						choice = ran.nextInt(4);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 1));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 2));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 1));
						}
						winType = "blockDraw";
						winSpec = "bestCase";
					}
				} 
			// Lastly if the player chose a side for both turns
			} else {
				// Check if the player chose a mirrored side then place a piece anywhere
				if((grid[0][1] == playerChar && grid[2][1] == playerChar) || (grid[1][0] == playerChar && grid[1][2] == playerChar)) {
					// Checks to see which places you can't place a piece
					if(grid[0][1] == playerChar || grid[2][1] == playerChar) {
						choice = ran.nextInt(6);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 0));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(1, 2));
						} else if (choice == 4) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						} else if (choice == 5) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						} 
					}
					// Checks to see which places you can't place a piece
					else if(grid[1][0] == playerChar || grid[1][2] == playerChar) {
						choice = ran.nextInt(6);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 1));
						} else if (choice == 3) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 1));
						} else if (choice == 4) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						} else if (choice == 5) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						} 
					}
					winType = "winCon";
					winSpec = "squeezeWin";
				}
				// Last option if player picked two adjacent sides, AI forces a draw
				else {
					// Checks which places you can put a piece for this scenario
					if(grid[1][0] == playerChar && grid[2][1] == playerChar) {
						choice = ran.nextInt(3);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						}
					}
					// Checks which places you can put a piece for this scenario
					else if(grid[1][0] == playerChar && grid[0][1] == playerChar) {
						choice = ran.nextInt(3);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						}
					}
					// Checks which places you can put a piece for this scenario
					else if(grid[1][2] == playerChar && grid[0][1] == playerChar) {
						choice = ran.nextInt(3);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 0));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						}
					}
					// Checks which places you can put a piece for this scenario
					else if(grid[1][2] == playerChar && grid[2][1] == playerChar) {
						choice = ran.nextInt(3);
						if(choice == 0) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 2));
						} else if (choice == 1) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(2, 0));
						} else if (choice == 2) {
							// AI calls placePiece to place the piece and print position
							System.out.println(placePiece(0, 2));
						}
					}
					winType = "blockDraw";
					winSpec = "bestCase";
				}
			}
		}
	}
	
	/* Invokes if the AI is guaranteed to win
	 * 
	 * See note pad for all type of winCons and winSpecs
	 * @param type of winCon
	 */
	public void winCon(String type) {
		// winSpec = midTrap, see note pad
		// winSpec = cornerTrap, see note pad
		if(type.equals("midTrap") || type.equals("cornerTrap")) {
			// On first move, AI sets up corner
			if(moves == 1) {
				choice = ran.nextInt(2);
				// Randomly chooses left
				if(choice == 0) {
					aicol = 0;
					choice = ran.nextInt(2);
					// Randomly chooses up
					if(choice == 0) {
						airow = 0;
					} else if(choice == 1) {
						airow = 2;
					}
				// Randomly chooses right
				} else if(choice == 1) {
					aicol = 2;
					choice = ran.nextInt(2);
					// Randomly chooses bottom
					if(choice == 0) {
						airow = 0;
					} else if(choice == 1) {
						airow = 2;
					}
				}
				// AI calls placePiece to place the piece and print position
				System.out.println(placePiece(airow, aicol));
			// After the first move, AI can try to win
			} else {
				// Sees if a win is possible via the checkAIWin method
				if(checkAIWin()) {
					// Calls the bestMove method to capitalize on a blunder, winning the game
					bestMove("blunder");
				// Sees if the other player can win and try to block it
				} else if(checkPlayerWin()) {
					// Calls the bestMove method to play defensive
					bestMove("block");
				// Sees which choice is best if neither the player or AI can win
				} else {
					// First tests to see if the corner piece is in the top row
					if(airow == 0) {
						// Tests to see if the opponent piece is below, if it is, then place it in the same row
						if(grid[1][aicol] == playerChar) {
							// Randomly chooses the first or second position in the row
							choice = ran.nextInt(2);
							if(choice == 0) {
								// AI calls placePiece to place the piece and print position
								System.out.println(placePiece(airow, 1));
							} else if(choice == 1) {
								// Depends on whether or not the corner piece is in the first or third column
								if(aicol == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(airow, 2));
								} else if(aicol == 2) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(airow, 0));
								}
							}
						// Mirrored if the opponent piece is next to rather than below
						} else if(grid[airow][1] == playerChar) {
							// Randomly chooses the first or second position in the column
							choice = ran.nextInt(2);
							if(aicol == 0) {
								if(choice == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(1, aicol));
								} else if(choice == 1) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(2, aicol));
								}
							} else if(aicol == 2) {
								// Depends on whether or not the corner piece is in the first or third column
								if(choice == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(1, aicol));
								} else if(choice == 1) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(2, aicol));
								}
							}
						}
					// Mirrored if the piece is in the second row, code is exact same as above but reversed
					} else if(airow == 2) {
						// Happens if the opponent piece is above, randomly chooses a position in row
						if(grid[1][aicol] == playerChar) {
							choice = ran.nextInt(2);
							if(choice == 0) {
								// AI calls placePiece to place the piece and print position
								System.out.println(placePiece(airow, 1));
							} else if(choice == 1) {
								if(aicol == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(airow, 2));
								} else if(aicol == 2) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(airow, 0));
								}
							}
						} else if(grid[airow][1] == playerChar) {
							// Happens if the opponent piece is next to, randomly chooses a position in column
							choice = ran.nextInt(2);
							if(aicol == 0) {
								if(choice == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(1, aicol));
								} else if(choice == 1) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(0, aicol));
								}
							} else if(aicol == 2) {
								if(choice == 0) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(1, aicol));
								} else if(choice == 1) {
									// AI calls placePiece to place the piece and print position
									System.out.println(placePiece(0, aicol));
								}
							}
						}
					}
				}
			} 
		}
		else if(type.equals("squeezeWin")) {
			// Sees if a win is possible via the checkAIWin method
			if(checkAIWin()) {
				// Calls the bestMove method to capitalize on a blunder, winning the game
				bestMove("blunder");
			// Sees if the other player can win and try to block it
			} else if(checkPlayerWin()) {
				// Calls the bestMove method to play defensive
				bestMove("block");
			// Sees which choice is best if neither the player or AI can win
			} else {
				choice = ran.nextInt(2);
				if(grid[2][1] == AIChar) {
					if(choice == 0) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 0));
					} else if (choice == 1) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 2));
					}
				} else if(grid[0][1] == AIChar) {
					if(choice == 0) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 0));
					} else if (choice == 1) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 2));
					}
				} else if(grid[1][0] == AIChar) {
					if(choice == 0) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 0));
					} else if (choice == 1) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 0));
					}
				} else if(grid[1][2] == AIChar) {
					if(choice == 0) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(2, 2));
					} else if (choice == 1) {
						// AI calls placePiece to place the piece and print position
						System.out.println(placePiece(0, 2));
					}
				}
			}
		}
	}
	
	/* Invokes if the AI tries to force a draw
	 * 
	 * See note pad for all type of blockDraws and winSpecs
	 * @param type of blockDraw
	 */
	public void blockDraw(String type) {
		// If the AI went first, AI draws using the draw logic tree
		if(type.equals("firstDraw")) {
			// Checks first to see if the AI can win through blunder
			if(checkAIWin()) {
				// Calls the bestMove method to capitalize on a blunder, winning the game
				bestMove("blunder");
			}
			// Checks to see if the board is a draw
			else if(checkDraw()) {
				drawGame();
			}
			// Sees if the other player can win and try to block it
			else if(checkPlayerWin()) {
				// Calls the bestMove method to play defensive
				bestMove("block");
			}
			// Sees which choice is best if neither the player or AI can win
			else if(checkBestFit()) {
				// AI calls placePiece to place the piece and print position
				System.out.println(placePiece(bestRow, bestCol));
			}
		// Truly the same code as above (follows the draw logic tree)
		// Distinguishes between if the AI will draw or if the player will draw
		} else if(type.equals("bestCase") || type.equals("trueBlock") || type.equals("possibleWin")) {
			// Checks first to see if the AI can win through blunder
			if(checkAIWin()) {
				// Calls the bestMove method to capitalize on a blunder, winning the game
				bestMove("blunder");
			}
			// Checks to see if the board is a draw
			else if(checkDraw()) {
				drawGame();
			}
			// Sees if the other player can win and try to block it
			else if(checkPlayerWin()) {
				// Calls the bestMove method to play defensive
				bestMove("block");
			}
			// Sees which choice is best if neither the player or AI can win
			else if(checkBestFit()) {
				// AI calls placePiece to place the piece and print position
				System.out.println(placePiece(bestRow, bestCol));
			}
			// If there is no best option, pick any available one
			else {
				chooseAvailable();
			}
		}
	}
	
	
	/* Checks to see if the Player can win
	 * 
	 * Sees if two Player pieces and an open space are in a row
	 * Checks horizontally, vertically, and diagonally
	 * Also sets the coordinates of where the winnable spot is
	 * @return T/F
	 */
	public boolean checkPlayerWin() {	
		// Defaults to false if no cases are true
		boolean winnable = false;
		// HORIZONTAL
		// Checks if two X's are in a row and makes sure the last is empty
		if((grid[0][0] == playerChar) && (grid[0][1] == playerChar || grid[0][2] == playerChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if((grid[0][1] == playerChar) && (grid[0][0] == playerChar || grid[0][2] == playerChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if((grid[0][2] == playerChar) && (grid[0][1] == playerChar || grid[0][0] == playerChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			}
		} if((grid[1][0] == playerChar) && (grid[1][1] == playerChar || grid[1][2] == playerChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][1] == playerChar) && (grid[1][0] == playerChar || grid[1][2] == playerChar)) {
			if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			} else if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][2] == playerChar) && (grid[1][1] == playerChar || grid[1][0] == playerChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			}
		} if((grid[2][0] == playerChar) && (grid[2][1] == playerChar || grid[2][2] == playerChar)) {
			if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][1] == playerChar) && (grid[2][0] == playerChar || grid[2][2] == playerChar)) {
			if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][2] == playerChar) && (grid[2][1] == playerChar || grid[2][0] == playerChar)) {
			if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		}
		
		// VERTICAL
		// Checks if two X's are in a column and makes sure the last is empty
		if((grid[0][0] == playerChar) && (grid[1][0] == playerChar || grid[2][0] == playerChar)) {
			if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if((grid[0][1] == playerChar) && (grid[1][1] == playerChar || grid[2][1] == playerChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			}
		} if((grid[0][2] == playerChar) && (grid[1][2] == playerChar || grid[2][2] == playerChar)) {
			if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][0] == playerChar) && (grid[0][0] == playerChar || grid[2][0] == playerChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if((grid[1][1] == playerChar) && (grid[0][1] == playerChar || grid[2][1] == playerChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			}
		} if((grid[1][2] == playerChar) && (grid[0][2] == playerChar || grid[2][2] == playerChar)) {
			if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][0] == playerChar) && (grid[0][0] == playerChar || grid[1][0] == playerChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			}
		} if((grid[2][1] == playerChar) && (grid[0][1] == playerChar || grid[1][1] == playerChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		} if((grid[2][2] == playerChar) && (grid[1][2] == playerChar || grid[0][2] == playerChar)) {
			if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		}
		
		// DIAGONAL
		// Checks if two X's are in a diagonal and makes sure the last is empty
		if(grid[0][0] == playerChar && grid[1][1] == playerChar) {
			if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if(grid[2][2] == playerChar && grid[1][1] == playerChar) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			}
		} if(grid[0][2] == playerChar && grid[1][1] == playerChar) {
			if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if(grid[2][0] == playerChar && grid[1][1] == playerChar) {
			if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if(grid[0][0] == playerChar && grid[2][2] == playerChar) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		} if(grid[0][2] == playerChar && grid[2][0] == playerChar) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		}
		return winnable;	
	}
	
	
	/* Checks to see if the AI can win
	 * 
	 * Sees if two AI pieces and an open space are in a row
	 * Checks horizontally, vertically, and diagonally
	 * Also sets the coordinates of where the winnable spot is
	 * @return T/F
	 */
	public boolean checkAIWin() {
		// Defaults to false if no cases are true
		boolean winnable = false;
		// HORIZONTAL
		// Checks if two O's are in a row and makes sure the last is empty
		if((grid[0][0] == AIChar) && (grid[0][1] == AIChar || grid[0][2] == AIChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if((grid[0][1] == AIChar) && (grid[0][0] == AIChar || grid[0][2] == AIChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if((grid[0][2] == AIChar) && (grid[0][1] == AIChar || grid[0][0] == AIChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			}
		} if((grid[1][0] == AIChar) && (grid[1][1] == AIChar || grid[1][2] == AIChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][1] == AIChar) && (grid[1][0] == AIChar || grid[1][2] == AIChar)) {
			if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			} else if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][2] == AIChar) && (grid[1][1] == AIChar || grid[1][0] == AIChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			}
		} if((grid[2][0] == AIChar) && (grid[2][1] == AIChar || grid[2][2] == AIChar)) {
			if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][1] == AIChar) && (grid[2][0] == AIChar || grid[2][2] == AIChar)) {
			if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][2] == AIChar) && (grid[2][1] == AIChar || grid[2][0] == AIChar)) {
			if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		}
		
		// VERTICAL
		// Checks if two O's are in a column and makes sure the last is empty
		if((grid[0][0] == AIChar) && (grid[1][0] == AIChar || grid[2][0] == AIChar)) {
			if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if((grid[0][1] == AIChar) && (grid[1][1] == AIChar || grid[2][1] == AIChar)) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			} else if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			}
		} if((grid[0][2] == AIChar) && (grid[1][2] == AIChar || grid[2][2] == AIChar)) {
			if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[1][0] == AIChar) && (grid[0][0] == AIChar || grid[2][0] == AIChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if((grid[1][1] == AIChar) && (grid[0][1] == AIChar || grid[2][1] == AIChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[2][1] == '-') {
				winRow = 2;
				winCol = 1;
				winnable = true;
			}
		} if((grid[1][2] == AIChar) && (grid[0][2] == AIChar || grid[2][2] == AIChar)) {
			if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			} else if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if((grid[2][0] == AIChar) && (grid[0][0] == AIChar || grid[1][0] == AIChar)) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			} else if(grid[1][0] == '-') {
				winRow = 1;
				winCol = 0;
				winnable = true;
			}
		} if((grid[2][1] == AIChar) && (grid[0][1] == AIChar || grid[1][1] == AIChar)) {
			if(grid[0][1] == '-') {
				winRow = 0;
				winCol = 1;
				winnable = true;
			} else if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		} if((grid[2][2] == AIChar) && (grid[1][2] == AIChar || grid[0][2] == AIChar)) {
			if(grid[1][2] == '-') {
				winRow = 1;
				winCol = 2;
				winnable = true;
			} else if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} 
		
		// DIAGONAL
		// Checks if two O's are in a diagonal and makes sure the last is empty
		if(grid[0][0] == AIChar && grid[1][1] == AIChar) {
			if(grid[2][2] == '-') {
				winRow = 2;
				winCol = 2;
				winnable = true;
			}
		} if(grid[2][2] == AIChar && grid[1][1] == AIChar) {
			if(grid[0][0] == '-') {
				winRow = 0;
				winCol = 0;
				winnable = true;
			}
		} if(grid[0][2] == AIChar && grid[1][1] == AIChar) {
			if(grid[2][0] == '-') {
				winRow = 2;
				winCol = 0;
				winnable = true;
			}
		} if(grid[2][0] == AIChar && grid[1][1] == AIChar) {
			if(grid[0][2] == '-') {
				winRow = 0;
				winCol = 2;
				winnable = true;
			}
		} if(grid[0][0] == AIChar && grid[2][2] == AIChar) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		} if(grid[0][2] == AIChar && grid[2][0] == AIChar) {
			if(grid[1][1] == '-') {
				winRow = 1;
				winCol = 1;
				winnable = true;
			}
		}
		// Return T/F
		return winnable;	
	}
	
	
	/* Checks to see what move can set up a win condition
	 * 
	 * Sees if two open spaces and an AI piece are in a row 
	 * Checks horizontally, vertically, and diagonally
	 * Randomly chooses which space if best
	 * Sets the coordinates of where the best spot is
	 */
	public boolean checkBestFit() {
		boolean fits = false;
		choice = ran.nextInt(2);
		// HORIZONTAL
		// Checks if two -'s are in the first row and makes sure the last is empty
		if((grid[0][0] == '-') && (grid[0][1] == '-' || grid[0][2] == '-')) {
			if(grid[0][1] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[0][2] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[0][1] == '-') && (grid[0][0] == '-' || grid[0][2] == '-')) {
			if(grid[0][0] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[0][2] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[0][2] == '-') && (grid[0][1] == '-' || grid[0][0] == AIChar)) {
			if(grid[0][1] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[0][0] == AIChar) {
				fits = true;
				bestRow = 0;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			}
		}
		// Checks if two -'s are in the second row and makes sure the last is empty
		if((grid[1][0] == '-') && (grid[1][1] == '-' || grid[1][2] == '-')) {
			if(grid[1][1] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[1][2] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[1][1] == '-') && (grid[1][0] == '-' || grid[1][2] == '-')) {
			if(grid[1][0] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[1][2] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[1][2] == '-') && (grid[1][1] == '-' || grid[1][0] == '-')) {
			if(grid[1][1] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[1][0] == AIChar) {
				fits = true;
				bestRow = 1;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			}
		} 
		// Checks if two -'s are in the second row and makes sure the last is empty
		if((grid[2][0] == '-') && (grid[2][1] == '-' || grid[2][2] == '-')) {
			if(grid[2][1] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[2][2] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[2][1] == '-') && (grid[2][0] == '-' || grid[2][2] == '-')) {
			if(grid[2][0] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[2][2] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 1;
				}
			}
		} if((grid[2][2] == '-') && (grid[2][1] == '-' || grid[2][0] == '-')) {
			if(grid[2][1] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 0;
				} else if(choice==1) {
					bestCol = 2;
				}
			} else if(grid[2][0] == AIChar) {
				fits = true;
				bestRow = 2;
				if(choice==0) {
					bestCol = 1;
				} else if(choice==1) {
					bestCol = 2;
				}
			}	
		}
		
		// VERTICAL
		// Checks if two X's are in a column and makes sure the last is empty
		if((grid[0][0] == '-') && (grid[1][0] == '-' || grid[2][0] == '-')) {
			if(grid[1][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[0][1] == '-') && (grid[1][1] == '-' || grid[2][1] == '-')) {
			if(grid[1][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[0][2] == '-') && (grid[1][2] == '-' || grid[2][2] == '-')) {
			if(grid[1][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[1][0] == '-') && (grid[0][0] == '-' || grid[2][0] == '-')) {
			if(grid[0][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[1][1] == '-') && (grid[0][1] == '-' || grid[2][1] == '-')) {
			if(grid[0][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[1][2] == '-') && (grid[0][2] == '-' || grid[2][2] == '-')) {
			if(grid[0][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[2][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 1;
				}
			}
		} if((grid[2][0] == '-') && (grid[0][0] == '-' || grid[1][0] == '-')) {
			if(grid[0][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[1][0] == AIChar) {
				fits = true;
				bestCol = 0;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			}
		} if((grid[2][1] == '-') && (grid[0][1] == '-' || grid[1][1] == '-')) {
			if(grid[0][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[1][1] == AIChar) {
				fits = true;
				bestCol = 1;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			}
		} if((grid[2][2] == '-') && (grid[1][2] == '-' || grid[0][2] == '-')) {
			if(grid[1][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 0;
				} else if(choice==1) {
					bestRow = 2;
				}
			} else if(grid[0][2] == AIChar) {
				fits = true;
				bestCol = 2;
				if(choice==0) {
					bestRow = 1;
				} else if(choice==1) {
					bestRow = 2;
				}
			}
		} 
		
		// DIAGONAL
		// Checks if two X's are in a diagonal and makes sure the last is empty
		if(grid[0][0] == '-' && grid[1][1] == '-') {
			if(grid[2][2] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 0;
					bestCol = 0;
				} else if(choice==1) {
					bestRow = 1;
					bestCol = 1;
				}
			}
		} if(grid[2][2] == '-' && grid[1][1] == '-') {
			if(grid[0][0] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 2;
					bestCol = 2;
				} else if(choice==1) {
					bestRow = 1;
					bestCol = 1;
				}
			}
		} if(grid[0][2] == '-' && grid[1][1] == '-') {
			if(grid[2][0] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 0;
					bestCol = 2;
				} else if(choice==1) {
					bestRow = 1;
					bestCol = 1;
				}
			}
		} if(grid[2][0] == '-' && grid[1][1] == '-') {
			if(grid[0][2] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 2;
					bestCol = 0;
				} else if(choice==1) {
					bestRow = 1;
					bestCol = 1;
				}
			}
		} if(grid[0][0] == '-' && grid[2][2] == '-') {
			if(grid[1][1] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 0;
					bestCol = 0;
				} else if(choice==1) {
					bestRow = 2;
					bestCol = 2;
				}
			}
		} if(grid[0][2] == '-' && grid[2][0] == '-') {
			if(grid[1][1] == AIChar) {
				fits = true;
				if(choice==0) {
					bestRow = 0;
					bestCol = 2;
				} else if(choice==1) {
					bestRow = 2;
					bestCol = 0;
				}
			}
		} 
		return fits;
	}
	
	
	/* Checks to see if the board is a draw
	 * 
	 * Places the final piece if the AI moved first
	 * Ends the game if the player moved first
	 * @return T/F
	 */
	public boolean checkDraw() {
		int counter = 0;
		boolean draw = false;
		// Checks to see how many spaces are full 
		if(grid[0][0] != '-') {
			counter++;
		} if(grid[0][1] != '-') {
			counter++;
		} if(grid[0][2] != '-') {
			counter++;
		} if(grid[1][0] != '-') {
			counter++;
		} if(grid[1][1] != '-') {
			counter++;
		} if(grid[1][2] != '-') {
			counter++;
		} if(grid[2][0] != '-') {
			counter++;
		} if(grid[2][1] != '-') {
			counter++;
		} if(grid[2][2] != '-') {
			counter++;
		}
		// If there's only one more space, place the last piece and return true
		if(counter == 8) {
			if(grid[0][0] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' top left.");
				grid[0][0] = AIChar;
			} else if(grid[0][1] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "'top middle.");
				grid[0][1] = AIChar;
			} else if(grid[0][2] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' top right.");
				grid[0][2] = AIChar;
			} else if(grid[1][0] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' middle left.");
				grid[1][0] = AIChar;
			} else if(grid[1][1] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' middle.");
				grid[1][1] = AIChar;
			} else if(grid[1][2] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' middle right.");
				grid[1][2] = AIChar;
			} else if(grid[2][0] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' bottom left.");
				grid[2][0] = AIChar;
			} else if(grid[2][1] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' middle.");
				grid[2][1] = AIChar;
			} else if(grid[2][2] == '-') {
				System.out.println("[SYS] AI places '" + AIChar + "' right.");
				grid[2][2] = AIChar;
			}
			printGrid();
			draw = true;
		// If there's no more space, return true
		} else if(counter==9){
			draw = true;
		}
		return draw;
	}
	
	
	public void chooseAvailable() {
		if(grid[0][0] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' top left.");
			grid[0][0] = AIChar;
		} else if(grid[0][1] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "'top middle.");
			grid[0][1] = AIChar;
		} else if(grid[0][2] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' top right.");
			grid[0][2] = AIChar;
		} else if(grid[1][0] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' middle left.");
			grid[1][0] = AIChar;
		} else if(grid[1][1] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' middle.");
			grid[1][1] = AIChar;
		} else if(grid[1][2] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' middle right.");
			grid[1][2] = AIChar;
		} else if(grid[2][0] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' bottom left.");
			grid[2][0] = AIChar;
		} else if(grid[2][1] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' middle.");
			grid[2][1] = AIChar;
		} else if(grid[2][2] == '-') {
			System.out.println("[SYS] AI places '" + AIChar + "' right.");
			grid[2][2] = AIChar;
		}
	}
	
	/* Chooses the AI's move after initial checks
	 * 
	 * blunder will win the game if checkAIWin() returns true
	 * block will stop the player from winning if checkPlayerWin() returns true
	 * @param move, the type of condition needed to call the right move
	 */
	public void bestMove(String move) {
		if(move.equals("blunder")) {
			// AI calls placePiece to place the winning piece and print position
			System.out.println(placePiece(winRow, winCol));
			printGrid();
			// Ends the game with AI as a winner
			endGame();
		} else if(move.equals("block")) {
			// AI calls placePiece to place the blocking piece and print position
			System.out.println(placePiece(winRow, winCol));
		}
	}

	
	/* Prints out the AI's move for player accessibility
	 * 
	 * Checks for the position and returns the correct placement string
	 * @param int row, int col - position of piece
	 * @return placement - placement of piece
	 */
	public String placePiece(int row, int col) {
		String placement = "";
		grid[row][col] = AIChar;
		if(row == 0) {
			if(col == 0) {
				placement = "[SYS] AI places '" + AIChar + "' top left.";
			} else if(col == 1) {
				placement = "AI places '" + AIChar + "' top.";
			} else if(col == 2) {
				placement = "AI places '" + AIChar + "' top right.";
			}
		} else if(row == 1) {
			if(col == 0) {
				placement = "[SYS] AI places '" + AIChar + "' middle left.";
			} else if(col == 1) {
				placement = "[SYS] AI places '" + AIChar + "' middle.";
			} else if(col == 2) {
				placement = "[SYS] AI places '" + AIChar + "' middle right.";
			}
		} else if(row == 2) {
			if(col == 0) {
				placement = "[SYS] AI places '" + AIChar + "' bottom left.";
			} else if(col == 1) {
				placement = "[SYS] AI places '" + AIChar + "' middle bottom.";
			} else if(col == 2) {
				placement = "[SYS] AI places '" + AIChar + "' bottom right.";
			}
		} 
		return placement;
	}
	
	/* Invokes if the game is a draw
	 * 
	 * Prints out the completed board
	 * Repeats if you want to play again
	 * Ends the game if not
	 */
	public void drawGame() {
		drawCount++;
		// Print result
		System.out.println("-------");
		printGrid();
		System.out.println("Cat's Game!");
		System.out.println("\nWins: 0 | Draws: " + drawCount + " | Losses: " + winCount);
		
		// Ask if the player is going again
		System.out.print("[SYS] Do you want to play again (y/n)? ");
		Scanner inputPrompt = new Scanner(System.in);
		String yn = inputPrompt.next();
		
		// Restarts game if yes
		if(yn.equalsIgnoreCase("yes") || yn.equalsIgnoreCase("y")) {
			startGame();
		// Ends game if no
		} else if(yn.equalsIgnoreCase("no") || yn.equalsIgnoreCase("n")) {
			System.out.println("[SYS] Game ended.");
			System.exit(0);
		// Repeat if input is invalid
		} else {
			System.out.println("[SYS] Cannot read input. Please answer yes or no.");
			promptGame();
		}
		inputPrompt.close();
	}
	
	/* Invokes if the AI is going to win
	 * 
	 * Player will never win; not how the AI works
	 * Ends the game
	 */
	public void endGame() {
		winCount++;
		// Print result
		System.out.println("-------");
		printGrid();
		System.out.println("AI wins!");
		System.out.println("\nWins: 0 | Draws: " + drawCount + " | Losses: " + winCount);
		
		// Ask if the player is going again
		System.out.print("[SYS] Do you want to play again (y/n)? ");
		Scanner inputPrompt = new Scanner(System.in);
		String yn = inputPrompt.next();
		
		// Restarts game if yes
		if(yn.equalsIgnoreCase("yes") || yn.equalsIgnoreCase("y")) {
			startGame();
		// Ends game if no
		} else if(yn.equalsIgnoreCase("no") || yn.equalsIgnoreCase("n")) {
			System.out.println("[SYS] Game ended.");
			System.exit(0);
		// Repeat if input is invalid
		} else {
			System.out.println("[SYS] Cannot read input. Please answer yes or no.");
			promptGame();
		}
		inputPrompt.close();
	}
	

}
