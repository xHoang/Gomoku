import java.awt.Color;

/** The random gomoku player chooses random squares on the board (using a
 *  uniform distribution) until an unoccupied square is found, which is then
 *  returned as the player's move. It is assumed that the board is not full,
 *  otherwise chooseMove() will get stuck in an infinite loop. 
 *	Author: Simon Dixon
 **/
class RandomPlayer extends GomokuPlayer {

	public Move chooseMove(Color[][] board, Color me) {
		while (true) {
			int row = (int) (Math.random() * 8);	// values are from 0 to 7
			int col = (int) (Math.random() * 8);
			if (board[row][col] == null)			// is the square vacant?
				return new Move(row, col);
		}
	} // chooseMove()

} // class RandomPlayer
