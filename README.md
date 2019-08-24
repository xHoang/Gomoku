# GomokoGame
Artificial Intelligence: Gomoku

## The aim

The aim of this assignment is to write a Java class to play the game Gomoku, or five-in-a-row, on an 8 ⇥ 8 board, using alpha-beta search. The game is played as follows: two players (white and black) take turns at placing a stone of his/her colour on an unoccupied square on the board (white moves first). The first player to complete a continuous horizontal, vertical or diagonal line of 5 or more stones of his/her colour is the winner (scoring 2 points). The loser scores 0. If all squares are occupied and neither player has won, then each player gets 1 point.

Your program will be given a time limit of 10 seconds per move (on the ITL computers). Any program which exceeds this limit will immediately forfeit the game to its opponent. Similarly any program which raises an exception or makes an illegal move (out of range or already occupied) will lose immediately. There are no other restrictions on moves (gomoku experts may be aware that some tournaments have further restrictions).

Your player class must be an instance of the abstract class GomokuPlayer, so you need to write a class which extends GomokuPlayer and 
overrides the method chooseMove():

      public Move chooseMove(Color[][] board, Color myColour)

The first argument is the current state of the board (each cell contains Color.black, Color.white or null), the second argument identifies which colour you are playing (Color.black or Color.white), and the return value is the move that your algorithm selects. The Move object has two fields, row and col, corresponding to a move at board[row][col], so both fields should be between 0 and 7. Use the constructor public Move(int row, int col) to set the fields. The names of all classes you define must end with your student number (e.g. Player120394567). I have supplied an example of a trivial player in RandomPlayer.java.

A Java class (GomokuReferee.class) is provided for developing and testing your player. This includes a graphical interface which allows you to play against your programme or watch games between computer players. It automatically finds any player classes which are in the same directory as you call java GomokuReferee from). GomokuReferee takes the following optional arguments:

	log — echo all moves to standard output for debugging or analysis of your player; games are selected manually using the GUI.
	batchTest — run a tournament where all players play against each other, without using the GUI; all moves and results of each game, plus a summary of the tournament, are printed to standard output. limit value — set the time limit per move to value (default 10.0 seconds).
	delay value — set the minimum time between displaying successive moves to value (default 1.0 seconds).

### How to run

1)Clone this repository in your local one 

2)cd from cmd into the directory

3)Run GomokuReferee class (thats where the main is)

4)Experiment with the algorithms provided for the game

5)You can use trivial algorithms provided (Sequence & Random) or play yourself!
