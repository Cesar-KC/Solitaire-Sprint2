package solitaire;

import org.junit.jupiter.api.Test;

import solitaire.Board;
import solitaire.BoardType;
import solitaire.CellState;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

	
	
    // ─────────────────────────────────────────
    // BOARD CONSTRUCTION TESTS
    // ─────────────────────────────────────────

    // English Size 5
    @Test
    public void testEnglishSize5_CenterIsEmpty() {
        Board board = new Board(5, BoardType.ENGLISH);
        assertEquals(CellState.EMPTY, board.getState(2, 2));
    }

    @Test
    public void testEnglishSize5_CornerIsInactive() {
        Board board = new Board(5, BoardType.ENGLISH);
        // cut = (5-3)/2 = 1, so [0][0] is inactive
        assertEquals(CellState.INACTIVE, board.getState(0, 0));
        assertEquals(CellState.INACTIVE, board.getState(0, 4));
        assertEquals(CellState.INACTIVE, board.getState(4, 0));
        assertEquals(CellState.INACTIVE, board.getState(4, 4));
    }

    @Test
    public void testEnglishSize5_ActiveCell() {
        Board board = new Board(5, BoardType.ENGLISH);
        // arm cell should be active
        assertEquals(CellState.ACTIVE, board.getState(0, 2));
        assertEquals(CellState.ACTIVE, board.getState(2, 0));
    }

    // English Size 7
    @Test
    public void testEnglishSize7_CenterIsEmpty() {
        Board board = new Board(7, BoardType.ENGLISH);
        assertEquals(CellState.EMPTY, board.getState(3, 3));
    }

    @Test
    public void testEnglishSize7_CornerIsInactive() {
        Board board = new Board(7, BoardType.ENGLISH);
        // cut = (7-3)/2 = 2, so [0][0],[0][1],[1][0],[1][1] are all inactive
        assertEquals(CellState.INACTIVE, board.getState(0, 0));
        assertEquals(CellState.INACTIVE, board.getState(1, 1));
        assertEquals(CellState.INACTIVE, board.getState(0, 6));
        assertEquals(CellState.INACTIVE, board.getState(6, 6));
    }

    @Test
    public void testEnglishSize7_ActiveCell() {
        Board board = new Board(7, BoardType.ENGLISH);
        assertEquals(CellState.ACTIVE, board.getState(0, 3));
        assertEquals(CellState.ACTIVE, board.getState(3, 0));
    }

    // English Size 9
    @Test
    public void testEnglishSize9_CenterIsEmpty() {
        Board board = new Board(9, BoardType.ENGLISH);
        assertEquals(CellState.EMPTY, board.getState(4, 4));
    }

    @Test
    public void testEnglishSize9_CornerIsInactive() {
        Board board = new Board(9, BoardType.ENGLISH);
        // cut = (9-3)/2 = 3
        assertEquals(CellState.INACTIVE, board.getState(0, 0));
        assertEquals(CellState.INACTIVE, board.getState(2, 2));
        assertEquals(CellState.INACTIVE, board.getState(0, 8));
        assertEquals(CellState.INACTIVE, board.getState(8, 8));
    }

    // Hexagon Size 7
    @Test
    public void testHexagonSize7_CenterIsEmpty() {
        Board board = new Board(7, BoardType.HEXAGON);
        assertEquals(CellState.EMPTY, board.getState(3, 3));
    }

    @Test
    public void testHexagonSize7_CornerIsInactive() {
        Board board = new Board(7, BoardType.HEXAGON);
        // top-left diagonal corner: row + col < cut(2)
        assertEquals(CellState.INACTIVE, board.getState(0, 0));
        assertEquals(CellState.INACTIVE, board.getState(0, 1));
        assertEquals(CellState.INACTIVE, board.getState(1, 0));
    }

    @Test
    public void testHexagonSize7_DiagonalBoundaryIsActive() {
        Board board = new Board(7, BoardType.HEXAGON);
        // [1][1]: row+col = 2, not < 2, so active
        assertEquals(CellState.ACTIVE, board.getState(1, 1));
    }

    // Board size and type getters
    @Test
    public void testBoard_GetSizeReturnsCorrectSize() {
        Board board = new Board(9, BoardType.ENGLISH);
        assertEquals(9, board.getSize());
    }

    @Test
    public void testBoard_GetTypeReturnsCorrectType() {
        Board board = new Board(7, BoardType.HEXAGON);
        assertEquals(BoardType.HEXAGON, board.getType());
    }

    
    
    // ─────────────────────────────────────────
    // MOVE VALIDATION TESTS
    // (testing board state changes directly)
    // ─────────────────────────────────────────

    @Test
    public void testValidMove_PegJumpsOverPegIntoEmptyCell() {
        Board board = new Board(7, BoardType.ENGLISH);
        // center [3][3] is EMPTY, [3][2] is ACTIVE
        // make [3][1] empty so we can jump [3][2] over... 
        // Instead: jump [3][2] down — set up manually
        // from [1][3] (ACTIVE) over [2][3] (ACTIVE) to [3][3] (EMPTY) — valid north to south jump
        assertEquals(CellState.ACTIVE, board.getState(1, 3));
        assertEquals(CellState.ACTIVE, board.getState(2, 3));
        assertEquals(CellState.EMPTY,  board.getState(3, 3));

        // apply move manually
        board.setEmptyState(1, 3);
        board.setEmptyState(2, 3);
        board.setActiveState(3, 3);

        assertEquals(CellState.EMPTY,  board.getState(1, 3));
        assertEquals(CellState.EMPTY,  board.getState(2, 3));
        assertEquals(CellState.ACTIVE, board.getState(3, 3));
    }

    @Test
    public void testValidMove_HorizontalJump() {
        Board board = new Board(7, BoardType.ENGLISH);
        // [3][3] EMPTY, [3][2] ACTIVE — jump [3][1] over [3][2] to [3][3]
        assertEquals(CellState.ACTIVE, board.getState(3, 1));
        assertEquals(CellState.ACTIVE, board.getState(3, 2));
        assertEquals(CellState.EMPTY,  board.getState(3, 3));

        board.setEmptyState(3, 1);
        board.setEmptyState(3, 2);
        board.setActiveState(3, 3);

        assertEquals(CellState.EMPTY,  board.getState(3, 1));
        assertEquals(CellState.EMPTY,  board.getState(3, 2));
        assertEquals(CellState.ACTIVE, board.getState(3, 3));
    }

    @Test
    public void testInvalidMove_CannotJumpIntoActiveCell() {
        Board board = new Board(7, BoardType.ENGLISH);
        // [3][4] is ACTIVE — jumping into it is invalid (not EMPTY)
        assertNotEquals(CellState.EMPTY, board.getState(3, 4));
    }

    @Test
    public void testInvalidMove_CannotJumpOverEmptyCell() {
        Board board = new Board(7, BoardType.ENGLISH);
        // manually empty [2][3] — now jumping over it should be invalid
        board.setEmptyState(2, 3);
        assertEquals(CellState.EMPTY, board.getState(2, 3));
        // over cell is not ACTIVE, so move is invalid
        assertNotEquals(CellState.ACTIVE, board.getState(2, 3));
    }

    
    
    
    
    // ─────────────────────────────────────────
    // GAME OVER DETECTION TESTS
    // (testing Board state that leads to game over)
    // ─────────────────────────────────────────

    @Test
    public void testNotGameOver_FreshBoard() {
        // A fresh board always has valid moves available
        Board board = new Board(7, BoardType.ENGLISH);
        // center is EMPTY, adjacent cells are ACTIVE — moves exist
        assertEquals(CellState.EMPTY,  board.getState(3, 3));
        assertEquals(CellState.ACTIVE, board.getState(3, 2));
        assertEquals(CellState.ACTIVE, board.getState(3, 1));
    }

    @Test
    public void testGameOver_OnePegLeft() {
        Board board = new Board(7, BoardType.ENGLISH);
        // set all cells to EMPTY except one
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (board.getState(row, col) == CellState.ACTIVE) {
                    board.setEmptyState(row, col);
                }
            }
        }
        // place single peg at center
        board.setActiveState(3, 3);

        // count pegs
        int pegs = 0;
        for (int row = 0; row < 7; row++)
            for (int col = 0; col < 7; col++)
                if (board.getState(row, col) == CellState.ACTIVE) pegs++;

        assertEquals(1, pegs);
    }

    @Test
    public void testGameOver_NoValidMoves() {
        Board board = new Board(7, BoardType.ENGLISH);
        // isolate two pegs far apart with no jumps possible
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (board.getState(row, col) == CellState.ACTIVE) {
                    board.setEmptyState(row, col);
                }
            }
        }
        // place two isolated pegs — no valid jumps possible
        board.setActiveState(0, 2);
        board.setActiveState(4, 4);

        // verify no adjacent active+empty pairs exist for either peg
        // [0][2]: check below — [1][2] and [2][2] are both EMPTY
        assertNotEquals(CellState.ACTIVE, board.getState(1, 2));
        assertNotEquals(CellState.ACTIVE, board.getState(2, 2));
    }
}
