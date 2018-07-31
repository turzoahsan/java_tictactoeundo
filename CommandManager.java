import java.util.Stack;

/** This class is the gatekeeper for all the Commands. It will execute
 * commands passed by the model and track them for the purposes of undo
 * and redo operations.
 * 
 * This class should be capable of supporting any arbitrary model so
 * long as the model has all state-changing operations invoked through
 * Command instances.
 * 
 * 
 * @author aarnott
 *
 */
public class CommandManager {
	
	private Stack<Command> undos = new Stack<Command>();
	private Stack<Command> redos = new Stack<Command>();
	
	/** Executes a Command instance and adds it to the
	 * list of undos that are available.
	 * 
	 * @param c An instance of the Command interface
	 */
	public void executeCommand(Command c) {
		c.execute();
		undos.push(c);
		redos.clear();
	}

	/** Returns true if there is at least one undoable Command
	 * available on the undo list.
	 * 
	 * @return
	 */
	public boolean isUndoAvailable() {
		return !undos.empty();
	}

	/** Undoes the next available command to undo. If four commands
	 * were executed, the undo operations for those commands will
	 * happen in reverse order with four calls to this method.
	 * 
	 * Preconditions:
	 * -> Undo stack must not be empty
	 * 
	 */
	public void undo() {
		assert(!undos.empty());
		Command command = undos.pop();
		command.undo();
		redos.push(command);
	}

	/** Returns true if there is at least one redoable Command
	 * available on the redo list.
	 * 
	 * @return
	 */
	public boolean isRedoAvailable() {
		return !redos.empty();
	}

	/** Redoes the next available command to redo.
	 * 
	 * Preconditions:
	 * -> Redo stack must not be empty
	 * 
	 */
	public void redo() {
		assert(!redos.empty());
		Command command = redos.pop();
		command.execute();
		undos.push(command);
	}


}
