package solitaire;


public class Board {
	
	private int size, cut, center;
	private BoardType type;
	CellState[][] board;
	
	public Board(int size, BoardType type) {
		
		this.size = size;
		this.type = type;
		board = new CellState[size][size];
		
		cut = (size - 3) / 2;
    	
		//fill cells, depending on size and type
		if (type == BoardType.ENGLISH) {
		
		    
			//iterate thru board cells, setting cells active(valid pegs), or inactive cells(not a part of board)
	    	for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {

					if ((row < cut && col < cut) || (row < cut && col >= size-cut) || (row >= size-cut && col < cut) || (row >= size-cut && col >= size-cut))  {
						board[row][col] = CellState.INACTIVE;
					} else {
						board[row][col] = CellState.ACTIVE;
					}
				}
	    	}
		
		} else if (type == BoardType.HEXAGON) {
    
			//iterate thru board cells, setting cells active(valid pegs), or inactive cells(not a part of board) Hexagon
	    	for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {

					if ((row + col < cut) || (row - col >= (size-cut)) || (row + (size-1-col) < cut) || ((size-1-row) + (size-1-col) < cut))  {
						board[row][col] = CellState.INACTIVE;
					} else {
						board[row][col] = CellState.ACTIVE;
					}
				}
	    	}
			
		} else if (type == BoardType.DIAMOND) {
			//ToDO, shape board into a valid Diamond board, 
		}
		
		//set center cell to empty
		center = size / 2;
		board[center][center] = CellState.EMPTY;
		
	}
	

	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public BoardType getType() {
		return type;
	}

	public void setType(BoardType type) {
		this.type = type;
	}
	
	public CellState getState(int row, int col){
		return board[row][col];
		
	}
	
	public CellState setEmptyState(int row, int col){
		return board[row][col] = CellState.EMPTY;
		
	}
	
	public CellState setActiveState(int row, int col){
		return board[row][col] = CellState.ACTIVE;
		
	}
	
	public CellState setInactiveState(int row, int col){
		return board[row][col] = CellState.INACTIVE;
		
	}
}

