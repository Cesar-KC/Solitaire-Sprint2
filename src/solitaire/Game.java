package solitaire;
import javax.swing.*;

import java.awt.*;
import java.util.Random;

public class Game extends JFrame {
	
	//initialize components
	//Default
	int size = 7;
	BoardType boardType = BoardType.ENGLISH;
	
	CellState cellType;
	JComboBox<String> cmbSize = new JComboBox<String>(new String[] {"5", "7", "9", "11"});
	JLabel lblBoardSize, lblBoardType, lblTitle, lblEmpty;
	JButton btnReplay, btnNewGame, btnAuto, btnRandom;
	JCheckBox cboxRecord;
	JRadioButton rbtnEnglish, rbtnHex, rbtnDiamond;
	Board board;
	JButton[][] buttons = new JButton[size][size];
	ButtonGroup group;
	JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	
	//game
	int selectedRow = -1;
	int selectedCol = -1;
	
	
	
	
	public Game() {
		
		cmbSize.setSelectedItem("7");
		
		buildUI();	
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Peg Solitaire");
		setLocationRelativeTo(null);
		
	}
	
	public void buildUI() {
		
		//Set Components
		lblBoardSize = new JLabel("Board Size");
		lblBoardType = new JLabel("Board Type");
		lblTitle = new JLabel("Peg Solitaire");
		lblEmpty = new JLabel("");
		cboxRecord = new JCheckBox("Record(In development)g");
		rbtnEnglish = new JRadioButton("English", true);
		rbtnHex = new JRadioButton("Hex");
		rbtnDiamond  = new JRadioButton("Diamond(In development)");
		board = new Board(size, boardType);
		
		
		//Buttons and action listeners for REPLAY GAME,AUTOPLAY, NEW GAME, and  RANDOM GAME
		btnReplay = new JButton("Replay");
		btnReplay.addActionListener(e -> {
		
			buildBoard(size, boardType);
		});
		
		btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(e -> {
			BoardType boardType = null;
			int size = Integer.parseInt((String) cmbSize.getSelectedItem());
			
			if (rbtnEnglish.isSelected()) { 
				boardType = BoardType.ENGLISH;
			}
	
			else if (rbtnHex.isSelected()) { 
				boardType = BoardType.HEXAGON;
			}
			else if (rbtnDiamond.isSelected()) { 
				//ToDO, implement Diamond logic in Board class 
				boardType = BoardType.DIAMOND;
			}
			
			buildBoard(size, boardType);
		});
		
		btnAuto = new JButton("AutoPlay");
		btnAuto.addActionListener(e -> {
			//ToDOL we need to create a functionality that allows for a computer to autoplay
		});
		
		btnRandom = new JButton("Randomize");
		btnRandom.addActionListener(e -> {
			Random random = new Random();
			int sizeSeed = random.nextInt(4) + 1;
			int typeSeed = random.nextInt(3) + 1;
			
			switch (sizeSeed) {
		    case 1:
		        size = 5;
		        break;
		    case 2:
		        size = 7;
		        break;
		    case 3:
		        size = 9;
		        break;
		    case 4:
		        size = 11;
		        break;
			}
			
		    switch (typeSeed) {
		    case 1:
		        boardType = BoardType.ENGLISH;
		        break;
		    case 2:
		    	boardType = BoardType.HEXAGON;
		        break;
//		    case 3:
//		    	boardType = BoardType.DIAMOND;
//		        break;
		    }
		    
		    buildBoard(size, boardType);
		});
	
		
		
		//Setting panels of frame 
		setLayout(new BorderLayout());
		
		//NORTH PANEL
		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northPanel.add(lblBoardSize);
		northPanel.add(cmbSize);
		northPanel.setBorder(BorderFactory.createEmptyBorder(8,4,8,8));

		//WEST PANEL
		westPanel = new JPanel(new GridLayout(4, 1));
		//create mutually exclusive selection
		group = new ButtonGroup();
		group.add(rbtnEnglish);
		group.add(rbtnHex); 
		group.add(rbtnDiamond);
		westPanel.add(lblBoardType);
		westPanel.add(rbtnEnglish);
		westPanel.add(rbtnHex);
		westPanel.add(rbtnDiamond);
		westPanel.setBorder(BorderFactory.createEmptyBorder(20,8,20,20));
	

		//EAST PANEL
		eastPanel = new JPanel(new GridLayout(4, 1, 8, 8));
		eastPanel.add(btnReplay);
		eastPanel.add(btnNewGame);
		eastPanel.add(btnAuto);
		eastPanel.add(btnRandom);
		eastPanel.setBorder(BorderFactory.createEmptyBorder(20,8,20,20));
		
		//SOUTH PANEL
		southPanel = new JPanel(new GridLayout(1, 1, 8, 8));
		southPanel.add(cboxRecord);
		southPanel.setBorder(BorderFactory.createEmptyBorder(20,8,20,20));
		
		
		//CENTER PANEL
		centerPanel = new JPanel(new GridLayout(size,size,20,20));
		
		//Create DEFAULT board, size =7, type = english
		for (int row = 0; row < size; row++) {
		    for (int col = 0; col < size; col++) {
		        int r = row;
		        int c = col;
		        
		        //create valids button based on board layout/size
		        if (board.getState(r, c) == CellState.ACTIVE ||
		            board.getState(r, c) == CellState.EMPTY) {

		            JButton cellButton;

		            if (board.getState(r, c) == CellState.ACTIVE) {
		                cellButton = new JButton("PEG");
		            } else {
		                cellButton = new JButton("EMPTY");
		            }
		            //button listener for peg, or empty buttons
		            cellButton.addActionListener(e -> {
		                // FIRST CLICK
		                if (selectedRow == -1 && selectedCol == -1) {
		                    if (board.getState(r, c) == CellState.ACTIVE) {
		                        selectedRow = r;
		                        selectedCol = c;
		                    }
		                }

		                // SECOND CLICK, selected row/columns have some value(isClicked)
		                else {
		                    int fromRow = selectedRow;
		                    int fromCol = selectedCol;
		                    int toRow = r;
		                    int toCol = c;
		                    
		                    //checks to make sure second click is an empty button
		                    //checks if moves stay in same row or columns, ie horizontal or veritcal move, no diagonal moves
//						    //if one is true, valid move if and only if move is 2 spots away respectively
		                    if (board.getState(toRow, toCol) == CellState.EMPTY) {
		                        boolean sameRow = fromRow == toRow;
		                        boolean sameCol = fromCol == toCol;
		                        boolean validDistance =
		                                (sameRow && Math.abs(toCol - fromCol) == 2) ||
		                                (sameCol && Math.abs(toRow - fromRow) == 2);
		                        
		                        //Valid distance is TRUE, so valid move
//						        //So, calculate position and CellState of peg we jump over
		                        if (validDistance) {
		                            int overRow = (fromRow + toRow) / 2;
		                            int overCol = (fromCol + toCol) / 2;
		                            
		                          //update board
		                            if (board.getState(overRow, overCol) == CellState.ACTIVE) {
		                                board.setEmptyState(fromRow, fromCol);
		                                board.setEmptyState(overRow, overCol);
		                                board.setActiveState(toRow, toCol);
		                                
		                                //refresh UI
		                                buildBoard(size, boardType);
		                            }
		                        }
		                    }
		                    
		                    //invalid second click, reset
		                    selectedRow = -1;
		                    selectedCol = -1;
		                }
		            });

		            buttons[r][c] = cellButton;
		            centerPanel.add(cellButton);
		        } else {
		            JLabel inactive = new JLabel("");
		            centerPanel.add(inactive);
		        }
		    }
		}
		
		//Add panels to frame
		add(northPanel,BorderLayout.NORTH);
		add(southPanel,BorderLayout.SOUTH);
		add(eastPanel,BorderLayout.EAST);
		add(westPanel,BorderLayout.WEST);
		add(centerPanel,BorderLayout.CENTER);

	}
	
	
	public void updateBoard() {
	    centerPanel.removeAll();
	    centerPanel.setLayout(new GridLayout(size, size, 20, 20));
	    buttons = new JButton[size][size];

	    selectedRow = -1;
	    selectedCol = -1;

	    for (int row = 0; row < size; row++) {
	        for (int col = 0; col < size; col++) {
	            int r = row;
	            int c = col;

	            if (board.getState(r, c) == CellState.ACTIVE ||
	                board.getState(r, c) == CellState.EMPTY) {

	                JButton cellButton;

	                if (board.getState(r, c) == CellState.ACTIVE) {
	                    cellButton = new JButton("Peg");
	                } else {
	                    cellButton = new JButton("EMPTY");
	                }

	                cellButton.addActionListener(e -> {
	                    if (selectedRow == -1 && selectedCol == -1) {
	                        if (board.getState(r, c) == CellState.ACTIVE) {
	                            selectedRow = r;
	                            selectedCol = c;
	                        }
	                    } else {
	                        int fromRow = selectedRow;
	                        int fromCol = selectedCol;
	                        int toRow = r;
	                        int toCol = c;

	                        if (board.getState(toRow, toCol) == CellState.EMPTY) {
	                            boolean sameRow = fromRow == toRow;
	                            boolean sameCol = fromCol == toCol;
	                            boolean validDistance =
	                                    (sameRow && Math.abs(toCol - fromCol) == 2) ||
	                                    (sameCol && Math.abs(toRow - fromRow) == 2);

	                            if (validDistance) {
	                                int overRow = (fromRow + toRow) / 2;
	                                int overCol = (fromCol + toCol) / 2;

	                                if (board.getState(overRow, overCol) == CellState.ACTIVE) {
	                                    board.setEmptyState(fromRow, fromCol);
	                                    board.setEmptyState(overRow, overCol);
	                                    board.setActiveState(toRow, toCol);
	                                    
	                                    updateBoard();
	                                    
	                                    //checks if game is over
	                                    //if so, count pegs to let player know if they won or now
	                                    if (isGameOver()) {
	                                        int pegs = numPegs();

	                                        if (pegs == 1) {
	                                            JOptionPane.showMessageDialog(this, "WINNER!");
	                                        } else {
	                                            JOptionPane.showMessageDialog(this,
	                                                    "Game over. ");
	                                        }
	                                    }
	                                    
	                                    return;
	                                }
	                            }
	                        }

	                        selectedRow = -1;
	                        selectedCol = -1;
	                    }
	                });

	                buttons[r][c] = cellButton;
	                centerPanel.add(cellButton);
	            } else {
	                JLabel inactive = new JLabel("");
	                centerPanel.add(inactive);
	            }
	        }
	    }

	    centerPanel.revalidate();
	    centerPanel.repaint();
	}
	
	public void buildBoard(int newSize, BoardType newType) {
	
		this.size = newSize;
	    this.boardType = newType;
	    this.board = new Board(newSize, newType);
	    updateBoard();
		
	}
	
	public boolean isGameOver() {
		//scans board for peg, then checks all surrounding positions to see if a move is possible
		//if not, game is over
		for (int row = 0; row < size; row++) {
	        for (int col = 0; col < size; col++) {
	            if (board.getState(row, col) == CellState.ACTIVE) {

	                // valid move above
	                if (row - 2 >= 0) {
	                    if (board.getState(row - 2, col) == CellState.EMPTY &&
	                        board.getState(row - 1, col) == CellState.ACTIVE) {
	                        return false;
	                    }
	                }

	                // valid move below
	                if (row + 2 < size) {
	                    if (board.getState(row + 2, col) == CellState.EMPTY &&
	                        board.getState(row + 1, col) == CellState.ACTIVE) {
	                        return false;
	                    }
	                }

	                // valid move to left
	                if (col - 2 >= 0) {
	                    if (board.getState(row, col - 2) == CellState.EMPTY &&
	                        board.getState(row, col - 1) == CellState.ACTIVE) {
	                        return false;
	                    }
	                }

	                // valid move to right
	                if (col + 2 < size) {
	                    if (board.getState(row, col + 2) == CellState.EMPTY &&
	                        board.getState(row, col + 1) == CellState.ACTIVE) {
	                        return false;
	                    }
	                }
	            }
	        }
		}
		//no valid moves, game over, return true
		 return true;
	}
	
	//method to count number of pegs left on board
	//if pegs left is 1, player wins!
	public int numPegs() {
	    int pegs = 0;

	    for (int row = 0; row < size; row++) {
	        for (int col = 0; col < size; col++) {
	            if (board.getState(row, col) == CellState.ACTIVE) {
	                pegs++;
	            }
	        }
	    }

	    return pegs;
	}
}
