/** Offers a command-line method of playing Tic-Tac-Toe.
 * 
 * This interpreter uses the word "command" to describe command-line input,
 * which isn't to be confused with the Command class in this program.
 * 
 * @author aarnott
 *
 */
public class TicTacToeCommandInterpreter {
	
	private enum ParseState {START, PLAYING, DONE}
	
	private TicTacToeModel model;
	private ParseState state;
	
	public TicTacToeCommandInterpreter() {
		model = new TicTacToeModel();
		state = ParseState.START;
		introduction();
	}
	
	private void introduction() {
		System.out.println("Tic-Tac-Toe Command-Line Game");
		System.out.println("-----------------------------");
		System.out.println("Enter '?' to repeat the following list of commands.");
		System.out.println();
		listCommands();
		System.out.println();
		showGrid();
		state = ParseState.PLAYING;
	}
	
	private void listCommands() {
		System.out.println("x row col - Put an X on the grid at the specified row and column");
		System.out.println("o row col - Put an O on the grid at the specified row and column");
		System.out.println("u - Undo the last move");
		System.out.println("r - Redo the last undone move");
		System.out.println();
	}
	
	public void parseCommand(String command) throws Exception {
		if(command.equals("")) {
			return;
		}
		if(command.equals("?")) {
			listCommands();
			return;
		}		
		
		switch(state) {
		case START:
			//Not used with the current states because we don't wait for input before
			//switching.
			break;
		case PLAYING:
			parseGameplayCommand(command);
			break;
		case DONE:
			//Also not used. Shows the general idea of states for parsing stuff.
			break;
		default:
			throw new Exception("Parser is in a weird state.");
		}
	}
	
	private void parseGameplayCommand(String command) throws Exception {
		char commandType = command.charAt(0);
		String[] tokens = command.split(" ");
		
		switch(commandType) {
		case 'x':
			if(!model.isPlayerXTurn()) {
				System.out.println("It isn't the X player's turn.");
				System.out.println();
				return;
			}			
			try {
				int row = Integer.parseInt(tokens[1]);
				int col = Integer.parseInt(tokens[2]);
				if(!model.isSpaceEmpty(row, col)) {
					System.out.println("That space is already filled.");
					System.out.println();
					return;	
				}
				model.placeX(row, col);
				if(model.isGameOver()) {
					endGame();
				} else {
					showGrid();
				}
			} catch (Exception e) {
				//Ignore exceptions basically
				System.out.println("Invalid command input.");
				System.out.println();
			}
			break;
		case 'o':
			if(!model.isPlayerOTurn()) {
				System.out.println("It isn't the O player's turn.");
				System.out.println();
				return;
			}
			try {
				int row = Integer.parseInt(tokens[1]);
				int col = Integer.parseInt(tokens[2]);
				if(!model.isSpaceEmpty(row, col)) {
					System.out.println("That space is already filled.");
					System.out.println();
					return;	
				}
				model.placeO(row, col);
				showGrid();
			} catch (Exception e) {
				//Ignore exceptions basically
				System.out.println("Invalid command input.");
				System.out.println();
			}	
			break;
		case 'u':
			if(model.canUndo()) {
				model.undo();
				System.out.println("The most recent move has been undone.");
				System.out.println();
				showGrid();
			} else {
				System.out.println("You can't undo any more moves.");
				System.out.println();	
			}
			break;
		case 'r':
			if(model.canRedo()) {
				model.redo();
				System.out.println("The most recent undo has been redone.");
				System.out.println();
				showGrid();
			} else {
				System.out.println("You can't redo any more moves.");
				System.out.println();	
			}			
			break;
		default:
			System.out.println("Invalid command. Enter '?' to see the possible commands.");
			System.out.println();
		}
	}
	
	
	private void showGrid() {		
		System.out.format( " %s | %s | %s%n", getGridCharacter(0, 0), getGridCharacter(0, 1), getGridCharacter(0, 2));
		System.out.println("-----------");
		System.out.format( " %s | %s | %s%n", getGridCharacter(1, 0), getGridCharacter(1, 1), getGridCharacter(1, 2));
		System.out.println("-----------");
		System.out.format( " %s | %s | %s%n", getGridCharacter(2, 0), getGridCharacter(2, 1), getGridCharacter(2, 2));
		System.out.println();
		
		
		if(model.isPlayerXTurn()) {
			System.out.println("It is currently the X player's turn. Enter a command:");
			System.out.println();
		} else {
			System.out.println("It is currently the O player's turn. Enter a command:");
			System.out.println();
		}
	}
	
	private String getGridCharacter(int row, int col) {
		if(model.isSpaceEmpty(row, col)) {
			return " ";
		} else if(model.isSpaceX(row, col)) {
			return "X";
		}
		return "O";
	}
	
	
	private void endGame() {
		if(model.hasPlayerXWon()) {
			System.out.println("The X player won. Restart the program to play again.");
			System.out.println();			
		} else if(model.hasPlayerOWon()) {
			System.out.println("The O player won. Restart the program to play again.");
			System.out.println();		
		} else {
			System.out.println("Cat's game. Restart the program to play again.");
			System.out.println();
		}
		
		state = ParseState.DONE;
		System.exit(0);
	}
	
	

}
