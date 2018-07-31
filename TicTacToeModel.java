/** The game logic for a Tic-Tac-Toe game. This model does not have
 * any hooks into the User Interface but could a notification system
 * to let the UI know something changed. This is a typical
 * Model-View-Controller design.
 * 
 * The game is represented by a simple 3x3 integer array. A value of
 * 0 means the space is empty, 1 means it is an X, 2 means it is an O. 
 * 
 * @author aarnott
 *
 */
public class TicTacToeModel {
	//True if it is the X player’s turn, false if it is the O player’s turn
	private boolean playerXTurn;
	//The set of spaces on the game grid
	private int[][] spaces;

	private CommandManager commandManager;
	
	/** Initialize a new game model. In the traditional Tic-Tac-Toe
	 * game, X goes first.
	 * 
	 */
	public TicTacToeModel() {
		spaces = new int[3][3]; 
		playerXTurn = true;
		commandManager = new CommandManager();
	}
	
	/** Returns true if it is the X player's turn.
	 * 
	 * @return
	 */
	public boolean isPlayerXTurn() {
		return playerXTurn;
	}
	
	/** Returns true if it is the O player's turn.
	 * 
	 * @return
	 */
	public boolean isPlayerOTurn() {
		return !playerXTurn;
	}
	
	/** Places an X on a space specified by the row and column
	 * parameters.
	 * 
	 * Preconditions:
	 * -> It must be the X player's turn
	 * -> The space must be empty
	 * 
	 * @param row The row to place the X on
	 * @param col The column to place the X on
	 */
	public void placeX(int row, int col) {
		assert(playerXTurn);
		assert(spaces[row][col] == 0);
		commandManager.executeCommand(new PlaceXCommand(this, row, col));
	}
	
	/** Places an O on a space specified by the row and column
	 * parameters.
	 * 
	 * Preconditions:
	 * -> It must be the O player's turn
	 * -> The space must be empty
	 * 
	 * @param row The row to place the O on
	 * @param col The column to place the O on
	 */	
	public void placeO(int row, int col) {
		assert(!playerXTurn);
		assert(spaces[row][col] == 0);
		commandManager.executeCommand(new PlaceOCommand(this, row, col));
	}
	
	/** Returns true if a space on the grid is empty (no Xs or Os)
	 * 	
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isSpaceEmpty(int row, int col) {
		return (spaces[row][col] == 0);
	}
	
	/** Returns true if a space on the grid is an X.
	 * 	
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isSpaceX(int row, int col) {
		return (spaces[row][col] == 1);
	}
	
	/** Returns true if a space on the grid is an O.
	 * 	
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isSpaceO(int row, int col) {
		return (spaces[row][col] == 2);
	}
	
	/** Returns true if the X player won the game. That is, if the
	 * X player has completed a line of three Xs.
	 * 
	 * @return
	 */
	public boolean hasPlayerXWon() {
		//Check rows
		if(spaces[0][0] == 1 && spaces[0][1] == 1 && spaces[0][2] == 1) return true;
		if(spaces[1][0] == 1 && spaces[1][1] == 1 && spaces[1][2] == 1) return true;
		if(spaces[2][0] == 1 && spaces[2][1] == 1 && spaces[2][2] == 1) return true;
		//Check columns
		if(spaces[0][0] == 1 && spaces[1][0] == 1 && spaces[2][0] == 1) return true;
		if(spaces[0][1] == 1 && spaces[1][1] == 1 && spaces[2][1] == 1) return true;
		if(spaces[0][2] == 1 && spaces[1][2] == 1 && spaces[2][2] == 1) return true;
		//Check diagonals
		if(spaces[0][0] == 1 && spaces[1][1] == 1 && spaces[2][2] == 1) return true;
		if(spaces[0][2] == 1 && spaces[1][1] == 1 && spaces[2][0] == 1) return true;
		//Otherwise, there is no line
		return false;
	}
	
	/** Returns true if the O player won the game. That is, if the
	 * O player has completed a line of three Os.
	 * 
	 * @return
	 */	
	public boolean hasPlayerOWon() {
		//Check rows
		if(spaces[0][0] == 2 && spaces[0][1] == 2 && spaces[0][2] == 2) return true;
		if(spaces[1][0] == 2 && spaces[1][1] == 2 && spaces[1][2] == 2) return true;
		if(spaces[2][0] == 2 && spaces[2][1] == 2 && spaces[2][2] == 2) return true;
		//Check columns
		if(spaces[0][0] == 2 && spaces[1][0] == 2 && spaces[2][0] == 2) return true;
		if(spaces[0][1] == 2 && spaces[1][1] == 2 && spaces[2][1] == 2) return true;
		if(spaces[0][2] == 2 && spaces[1][2] == 2 && spaces[2][2] == 2) return true;
		//Check diagonals
		if(spaces[0][0] == 2 && spaces[1][1] == 2 && spaces[2][2] == 2) return true;
		if(spaces[0][2] == 2 && spaces[1][1] == 2 && spaces[2][0] == 2) return true;
		//Otherwise, there is no line
		return false;
	}
	
	/** Returns true if all the spaces are filled or one of the players has
	 * won the game.
	 * 
	 * @return
	 */
	public boolean isGameOver() {
		if(hasPlayerXWon() || hasPlayerOWon()) return true;
		//Check if all the spaces are filled. If one isn’t the game isn’t over
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				if(spaces[row][col] == 0) return false;
			}
		}
		//Otherwise, it is a “cat’s game”
		return true;
	}
	
	/** Expose the undo list. This method could be used to add game logic
	 * restricting the number of undos allowed without messing with the
	 * CommandManager.
	 * 
	 * @return True if an undo operation is allowed
	 */
	public boolean canUndo() {
		return commandManager.isUndoAvailable();
	}
	
	/** Performs an undo operation.
	 * 
	 */
	public void undo() {
		commandManager.undo();
	}
	
	/** Expose the redo list. This method could be used to add game logic
	 * restricting the number of redos allowed without messing with the
	 * CommandManager.
	 * 
	 * @return True if an redo operation is allowed
	 */
	public boolean canRedo() {
		return commandManager.isRedoAvailable();
	}
	
	/** Performs an redo operation.
	 * 
	 */
	public void redo() {
		commandManager.redo();
	}	
	
	
	/** This Command implementation will place an X on the supplied
	 * row and column of the grid when executed. It will remove the
	 * X and restore the spaces previous value when undone (in the
	 * case of Tic-Tac-Toe, that means it will make the space empty,
	 * but it is more robust to restore the previous state exactly
	 * rather than assuming the rules of the game will stay the same).
	 * 
	 * @author aarnott
	 *
	 */
	private class PlaceXCommand implements Command {
		private TicTacToeModel model;
		private int previousValue;
		private boolean previousTurn;
		private int row;
		private int col;
		
		/** Constructs a PlaceXCommand.
		 * 
		 * @param model The TicTacToeModel that the command relates to
		 * @param row The row that an X will be placed on
		 * @param col The column the X will be placed on
		 */
		private PlaceXCommand(TicTacToeModel model, int row, int col) {
			this.model = model;
			this.row = row;
			this.col = col;
			//Copy the previous value from the grid
			this.previousValue = model.spaces[row][col];
			this.previousTurn = model.playerXTurn;
		}
		
		public void execute() {
			model.spaces[row][col] = 1;		
			model.playerXTurn = false;
		}
		
		public void undo() {
			model.spaces[row][col] = previousValue;
			model.playerXTurn = previousTurn;
		}		
	}
	
	/** This Command implementation will place an O on the supplied
	 * row and column of the grid when executed. It will remove the
	 * O and restore the spaces previous value when undone (in the
	 * case of Tic-Tac-Toe, that means it will make the space empty,
	 * but it is more robust to restore the previous state exactly
	 * rather than assuming the rules of the game will stay the same).
	 * 
	 * @author aarnott
	 *
	 */
	private class PlaceOCommand implements Command {
		private TicTacToeModel model;
		private int previousValue;
		private boolean previousTurn;
		private int row;
		private int col;
		
		/** Constructs a PlaceXCommand.
		 * 
		 * @param model The TicTacToeModel that the command relates to
		 * @param row The row that an X will be placed on
		 * @param col The column the X will be placed on
		 */
		private PlaceOCommand(TicTacToeModel model, int row, int col) {
			this.model = model;
			this.row = row;
			this.col = col;
			//Copy the previous value from the grid
			this.previousValue = model.spaces[row][col];
			this.previousTurn = model.playerXTurn;
		}
		
		public void execute() {
			model.spaces[row][col] = 2;		
			model.playerXTurn = true;
		}
		
		public void undo() {
			model.spaces[row][col] = previousValue;
			model.playerXTurn = previousTurn;
		}		
	}	
	

}