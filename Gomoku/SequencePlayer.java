import java.awt.Color;

/** This gomoku player chooses the first empty square, starting from the top
 *  left and moving across and then down to search the board.
 *  Author: Simon Dixon
 **/
class SequencePlayer extends GomokuPlayer {

	public Move chooseMove(Color[][] board, Color me) {
		for (int row = 0; row < GomokuBoard.ROWS; row++)
			for (int col = 0; col < GomokuBoard.COLS; col++)
				if (board[row][col] == null)
					return new Move(row, col);
		return null;
	} // chooseMove()

} // class SequencePlayer
