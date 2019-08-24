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

### Strategy

The Gomoku AI is implemented with minimax with alpha-beta pruning. The AI works by
utilising the minimax method which has one new parameter that differs from the pseudocode presented in the slides. This would be a time Counter which makes sure each move
lasts 10 seconds otherwise just returns our values. It will start by an initialising call to
minimax where it’s values will be returned in an integer array containing three values– row,
column and total score respectively.

The Algorithm works by getting all the legal moves (empty spaces) on the board and then
temporarily. The evaluation function is quite a simple function where it will just add a running
total where the evaluator will recognise chains of length one to five. From one to five
respectively, a larger arbitrary amount will be added to the running total and this score will
determine if the current move is the “best move” to utilise. Naturally if it finds a chain of five
in any direction it will return a very large number relative to its predecessors which means
this score will always take precedence

Moving into more detail about the function it will check its adjacent same coloured stones in
this fashion: Column, rows and then both directions. It does this by utilising three for loops
where the outer loop will have counted any chains it may see through a variable called
“consecutive”. The two inner loops will iterate through the rows and columns of the board
with the added temporary “move” during the minimax call. It will then allocate 1, 800 and
24000 points respectively for chains of one two and three. When it notices chains of four it
must check if the chain: Has both ends open, if only one side is open, if both sides are
closed, check if one side is blocked and is next to a grid border and check this for both the
current player and opponent.

The points for the counterpart opponent will also take away the same value away from the
running total in order to accommodate if the move will be great relative to the stones around
it. This will be zero sum and will then be checked back with the minimax call which it will then
be put through alpha-beta pruning where it checks if the initial value is larger than negative
infinity (since the AI will be the maximisingPlayer)

To accommodate for the “brute-force” method I have incorporated I need to make it efficient
as possible. A depth of four is the maximum is can be without further optimisations. A few
optimisations which can be done would be prioritising long chain firsts and identifying them
which means a full search with the for loop would not be necessary. It is also important to
check if the temporary move is a winning move and so I have included a hasWon() function
which will quickly check if there is a winning chain on the board. If so, it will the return the
move where it has made the winning chain.


