import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * @author aarnott
 *
 */
public class TicTacToeApplet extends JApplet {
	//Just a Java-ism. Feel free to ignore :).
	private static final long serialVersionUID = 7262842279499192167L;

	/** Applet entry point.
	 * 
	 */
	public void init() {
		//Build the gui when the event dispatching thread is ready
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					model = new TicTacToeModel();
					buildAppletUI();
				}		
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TicTacToeModel model;
	
	private JLabel[][] spaces;
	private JLabel status;
	private JButton undo;
	private JButton redo;
	
	/** Builds the UI.
	 * 
	 * The layout for this applet will be to have a tic-tac-toe board at the
	 * top using a 3x3 GridLayout. At the bottom will be undo/redo buttons and
	 * a text box to show which player's turn this is.
	 *
	 * So the general widget tree will be:
	 * 
	 * BorderLayout
	 * --North: BoxLayout
	 * ----JButton resetGame
	 * --Center: GridLayout (3x3)
	 * ----JLabel[][] spaces
	 * --South: BorderLayout
	 * ----JButton undo
	 * ----JLabel status
	 * ----JButton redo
	 * 
	 */
	private void buildAppletUI() {
		super.setSize(400, 400);
		super.setLayout(new BorderLayout());
		
		//North		
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
		
		JButton resetGame = new JButton("Restart Game");
		resetGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetGameClicked();
			}
		});
		resetGame.setSize(60, 20);
		//Right align the reset button
		north.add(Box.createHorizontalGlue());
		north.add(resetGame);
		
		//Center
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(3, 3));
		spaces = new JLabel[3][3];
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				spaces[x][y] = new JLabel("", JLabel.CENTER);
				//Styles for the X & Os
				(spaces[x][y]).setFont(new Font("monospace", Font.PLAIN, 70));
				int borderLeft = (x == 0 ? 0 : 4);
				int borderRight = (x == 2 ? 0 : 4);
				int borderTop = (y == 0 ? 0 : 4);
				int borderBottom = (y == 2 ? 0 : 4);
				(spaces[x][y]).setBorder(BorderFactory.createMatteBorder(borderTop, borderLeft, borderBottom, borderRight, Color.BLACK));
				
				final int spaceX = x;
				final int spaceY = y;
				(spaces[x][y]).addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						spaceClicked(spaceX, spaceY);
					}
				});
				
				center.add(spaces[x][y]);
			}
		}
		
		//South
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		
		undo = new JButton("Undo");
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				undoClicked();
			}
		});
		undo.setSize(60, 20);		
		
		status = new JLabel();
		
		redo = new JButton("Redo");
		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				redoClicked();
			}
		});
		redo.setSize(60, 20);
		
		south.add(undo, BorderLayout.WEST);
		south.add(status, BorderLayout.CENTER);
		south.add(redo, BorderLayout.EAST);
		
		super.add(north, BorderLayout.NORTH);
		super.add(center, BorderLayout.CENTER);
		super.add(south, BorderLayout.SOUTH);
		//GridLayout
	}
	
	private void resetGameClicked() {
		model = new TicTacToeModel();
		status.setText("");
		updateGrid();
	}
	
	private void undoClicked() {
		if(!model.canUndo()) { 
			status.setText("There are no moves to undo.");
			return;
		}
		model.undo();
		updateGrid();
		if(model.isPlayerXTurn()) {
			status.setText("Move undone. It is now the X player's turn.");
		} else {
			status.setText("Move undone. It is now the O player's turn.");
		}
	}
	
	private void redoClicked() {
		if(!model.canRedo()) { 
			status.setText("There are no moves to redo.");
			return;
		}
		model.redo();
		updateGrid();
		if(model.isPlayerXTurn()) {
			status.setText("Move redone. It is now the X player's turn.");
		} else {
			status.setText("Move redone. It is now the O player's turn.");
		}
	}
	
	private void spaceClicked(int x, int y) {
		if(model.isGameOver()) {
			return;
		}
		
		if(!model.isSpaceEmpty(y, x)) {
			status.setText("That space is already filled.");
			return;
		}
		if(model.isPlayerXTurn()) {
			model.placeX(y, x);
			status.setText("It is now the O player's turn.");
		} else {
			model.placeO(y, x);
			status.setText("It is now the X player's turn.");
		}
		updateGrid();
		if(model.isGameOver()) {
			if(model.hasPlayerXWon()) {
				status.setText("Game over. The X player won.");	
			} else if(model.hasPlayerOWon()) {
				status.setText("Game over. The O player won.");
			} else {
				status.setText("Game over. Cat's game.");
			}
			
		}
	}
		
		
	private void updateGrid() {
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				if(model.isSpaceEmpty(y, x)) {
					(spaces[x][y]).setText("");
				} else if(model.isSpaceX(y, x)) {
					(spaces[x][y]).setForeground(new Color(225, 55, 0));
					(spaces[x][y]).setText("X");
				} else if(model.isSpaceO(y, x)) {
					(spaces[x][y]).setForeground(Color.BLUE);
					(spaces[x][y]).setText("O");
				}				
			}
		}
	}

}
