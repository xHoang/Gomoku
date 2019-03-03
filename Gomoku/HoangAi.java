import java.awt.Color;
import java.util.*;

/**
 * My Code that runs the AI utilising Minimax / Alpha - Beta pruning
 * Author: Hoang Le
 **/
class HoangAi extends GomokuPlayer {

    long timeCounter;
    long maxTime = (long) 10000;
    int[][] emptyBoard;
    int negInf = -9999999;
    int posInf = 9999999;

    @Override
    public Move chooseMove(Color[][] board, Color myColor) {
        timeCounter = System.currentTimeMillis();
        emptyBoard = null;
        int[] bestMove;
        int depth = 4;
        boolean maximisingPlayer = true;

        bestMove = miniMax(board, myColor, depth, negInf, posInf, timeCounter, maximisingPlayer);
        if(board[bestMove[0]][bestMove[1]] == null){
            return new Move(bestMove[0], bestMove[1]);


        }
        while (true) {
            int row = (int) (Math.random() * 8);	// values are from 0 to 7
            int col = (int) (Math.random() * 8);
            if (board[row][col] == null) {        // is the square vacant?
                return new Move(row, col);
            }

        }
    } // chooseMove()

    public int[] miniMax(Color[][] board, Color myColor, int depth, int alpha, int beta, long timeCounter, boolean maximisingPlayer) {
        int totalScore = 0;
        Color opponent;
        if (myColor == Color.WHITE) {
            opponent = Color.BLACK;
        } else {
            opponent = Color.WHITE;
        }
        long currTime = System.currentTimeMillis() - timeCounter;
        if (currTime >= maxTime) {
            return new int[]{0, 0, totalScore};
        }

        if (depth == 0) {
            totalScore = evaluateBoard(board, myColor);
            return new int[]{0, 0, totalScore};
        }


        ArrayList<int[]> legalMoves = legalMoves(board, myColor);

        if (legalMoves.isEmpty()) {
            totalScore = evaluateBoard(board, myColor);
            return new int[]{0, 0, totalScore};
        }


        if (maximisingPlayer) {
            int[] best = new int[]{0, 0, negInf};
            for (int[] move : legalMoves) {

                int row = move[0];
                int col = move[1];
                board[row][col] = myColor;
                if(hasWon(board, myColor)){
                    board[row][col] = null;
                    return new int[]{row,col,posInf};
                }
                int[] res = miniMax(board, opponent, depth - 1, alpha, beta, timeCounter, false);
                if (res[2] > best[2]) {
                    best = new int[]{row, col, res[2]};
                }
                board[row][col] = null;

                if (alpha < best[2]) {
                    alpha = best[2];
                }
                if (beta <= alpha) {
                    return best;
                }
            }
            return best;
        } else {
            int[] best = new int[]{0, 0, posInf};
            for (int[] move : legalMoves) {
                int row = move[0];
                int col = move[1];
                board[row][col] = myColor;
                if(hasWon(board, myColor)){
                    board[row][col] = null;
                    return new int[]{row,col,negInf};
                }
                int[] res = miniMax(board, opponent, depth - 1, alpha, beta, timeCounter, true);
                if (res[2] < best[2]) {
                    best = new int[]{row, col, res[2]};
                }
                board[row][col] = null;
                if (best[2] < beta) {
                    beta = best[2];
                }
                if (beta <= alpha) {
                    return best;
                }
            }
            return best;
        }

    } // End of minimax()




    /*
    This is where each board state will get evaluated and be given a "score" (total score)
    I will specfically be looking for points where it the algorithm will see where the
    black and white stones will be placed. For example, if three of the stones will be
    placed in a row then it will be given a point if it found a state where it saw three
    stones.
    */
    public int evaluateBoard(Color[][] board, Color myColor) {
        Color opponent;
        if (myColor == Color.BLACK) {
            opponent = Color.WHITE;
        } else {
            opponent = Color.BLACK;
        }
        int totalPoints = 0;

        for (int consecutive = 1; consecutive <= 5; consecutive++) {
            // Check for every column
            for (int col = 0; col < 8; col++) {
                for (int row = 0; row <= 8 - consecutive; row++) {
                    if (consecutive == 1) {
                        if (myColor == board[row][col]) {
                            totalPoints = totalPoints + 1;
                        } else if (opponent == board[row][col]) {
                            totalPoints = totalPoints - 1;
                        }
                    } else if (consecutive == 2) {
                        if (myColor == board[row][col] && myColor == board[row + 1][col]) {
                            totalPoints = totalPoints + 800;
                        } else if (opponent == board[row][col] && opponent == board[row + 1][col]) {
                            totalPoints = totalPoints - 800;
                        }

                    } else if (consecutive == 3) {
                        if (myColor == board[row][col] && myColor == board[row + 1][col] && myColor == board[row + 2][col]) {
                            totalPoints = totalPoints + 24000;
                        } else if (opponent == board[row][col] && opponent == board[row + 1][col] && opponent == board[row + 2][col]) {
                            totalPoints = totalPoints - 24000;
                        }

                    } else if (consecutive == 4) {
                        if (myColor == board[row][col] && myColor == board[row + 1][col] && myColor == board[row + 2][col] && myColor == board[row + 3][col]) {
                        /*
                         This is where it gets tricky because I need to check what type of four in a row the stores is.
                         If I only check if something is in a row I won't know if it's a good move
                         since things can be blocked. Hence I need a way to test that:
                         - Check if both ends are open
                         - Check if only one side is open
                         - Check if both sides are closed
                         - Check if one side is blocked and is next to grid border
                         - Check it for both myself (the AI) and the opponent
                        */
                            if ((row + 4) < 8) {
                                if (board[row + 4][col] == null) { // check if empty on right side
                                    if ((row - 1) >= 0) { // check if at border
                                        if (board[row - 1][col] == null) {
                                            totalPoints = totalPoints + 6000000; // Both sides are now open so piortise this move
                                        } else {
                                            totalPoints = totalPoints + 800000; // only one open side which is pretty good
                                        }

                                    } else { // blocked by grid so only one side open
                                        totalPoints = totalPoints + 800000;
                                    }

                                } else { // check blocked since row+4 / col is not null
                                    if ((row - 1) >= 0) { // check if at border
                                        if (board[row - 1][col] == null) {
                                            totalPoints = totalPoints + 800000; // this is good we have four in a row but blocked one side only
                                        } else {
                                            // Do nothing blocked on both sides no way to win
                                        }
                                    } else {
                                        // Do nothing
                                    }
                                }
                            }
                            // No other spot in the 8x8 row currently
                            else if ((row - 1) >= 0) {
                                if (board[row - 1][col] == null) {
                                    totalPoints = totalPoints + 800000; // one side is open since null
                                } else {
                                    // Do nothing cant win as blocked
                                }

                            } else {
                                // Do Nothing
                            }

                        }
                        // Now we check for the opponents side
                        else if (opponent == board[row][col] && opponent == board[row + 1][col] && opponent == board[row + 2][col] && opponent == board[row + 3][col]) {

                            if ((row + 4) < 8) {
                                if (board[row + 4][col] == null) { // check if empty on right side
                                    if ((row - 1) >= 0) { // check if at border
                                        if (board[row - 1][col] == null) {
                                            totalPoints = totalPoints - 6000000; // Both sides are now open so piortise this move
                                        } else {
                                            totalPoints = totalPoints - 800000; // only one open side which is pretty good
                                        }

                                    } else { // blocked by grid so only one side open
                                        totalPoints = totalPoints - 800000;
                                    }

                                } else { // check blocked since row+4 / col is not null
                                    if ((row - 1) >= 0) { // check if at border
                                        if (board[row - 1][col] == null) {
                                            totalPoints = totalPoints - 800000; // this is good we have four in a row but blocked one side only
                                        } else {
                                            // Do nothing blocked on both sides no way to win
                                        }
                                    } else {
                                        // Do nothing
                                    }
                                }
                            }
                            // No other spot in the 8x8 row currently
                            else if ((row - 1) >= 0) {
                                if (board[row - 1][col] == null) {
                                    totalPoints = totalPoints - 800000; // one side is open since null
                                } else {
                                    // Do nothing cant win as blocked
                                }

                            } else {
                                // Do Nothing
                            }

                        } else {
                            // Do nothing as first if statement fails
                        }

                    } else if (consecutive == 5) {
                        if (myColor == board[row][col] && myColor == board[row + 1][col] && myColor == board[row + 2][col] && myColor == board[row + 3][col] && myColor == board[row + 4][col]) {
                            totalPoints = totalPoints + 10000000;
                           // break;
                        } else if (opponent == board[row][col] && opponent == board[row + 1][col] && opponent == board[row + 2][col] && opponent == board[row + 3][col] && opponent == board[row + 4][col]) {
                            totalPoints = totalPoints - 10000000;
                           //break;
                        }

                    }
                }// end check for every row
            }
            // check for rows
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col <= 8 - consecutive; col++) {
                    if (consecutive == 2) {
                        if (board[row][col] == myColor && board[row][col + 1] == myColor) {
                            totalPoints = totalPoints + 800;
                        } else if (board[row][col] == opponent && opponent == board[row][col + 1]) {
                            totalPoints = totalPoints - 800;
                        }
                    } else if (consecutive == 3) {
                        if (board[row][col] == myColor && board[row][col + 1] == myColor && board[row][col + 2] == myColor) {
                            totalPoints = totalPoints + 24000;
                        } else if (board[row][col] == opponent && board[row][col + 1] == opponent && board[row][col + 2] == opponent) {
                            totalPoints = totalPoints - 24000;
                        }
                    } else if (consecutive == 4) {
                        if (board[row][col] == myColor && board[row][col + 1] == myColor && board[row][col + 2] == myColor && board[row][col + 3] == myColor) {
                            if ((col + 4) < 8) {
                                if (board[row][col + 4] == null) {
                                    if ((col - 1) >= 0) {
                                        if (board[row][col - 1] == null) {
                                            totalPoints = totalPoints + 6000000; //  High score as both sides (top) are open
                                        } else {
                                            totalPoints = totalPoints + 800000; // one side is open!!
                                        }
                                    } else {
                                        totalPoints = totalPoints + 800000; // one side is still open
                                    }
                                } else { // check if blocked
                                    if ((col - 1) >= 0) {
                                        if (board[row][col - 1] == null) {
                                            totalPoints = totalPoints + 800000; // one side is open
                                        } else {
                                            // Do nothing
                                        }
                                    } else {
                                        // Do Nothing
                                    }


                                }
                            }
                        } else if (board[row][col] == opponent && board[row][col + 1] == opponent && board[row][col + 2] == opponent && board[row][col + 3] == opponent) {
                            if ((col + 4) < 8) {
                                if (board[row][col + 4] == null) {
                                    if ((col - 1) >= 0) {
                                        if (board[row][col - 1] == null) {
                                            totalPoints = totalPoints - 6000000; //  High score as both sides (top) are open
                                        } else {
                                            totalPoints = totalPoints - 800000; // one side is open!!
                                        }
                                    } else {
                                        totalPoints = totalPoints - 800000; // one side is still open
                                    }
                                } else { // check if blocked
                                    if ((col - 1) >= 0) {
                                        if (board[row][col - 1] == null) {
                                            totalPoints = totalPoints - 800000; // one side is open
                                        } else {
                                            // Do nothing
                                        }
                                    } else {
                                        // Do Nothing
                                    }


                                }
                            }
                        }

                    } else if (consecutive == 5) {
                        if (myColor == board[row][col] && myColor == board[row][col + 1] && myColor == board[row][col + 2] && myColor == board[row][col + 3] && myColor == board[row][col + 4]) {
                            totalPoints = totalPoints + 10000000;
                           // break;
                        } else if (opponent == board[row][col] && opponent == board[row][col + 1] && opponent == board[row][col + 2] && opponent == board[row][col + 3] && opponent == board[row][col + 4]) {
                            totalPoints = totalPoints - 10000000;
                           // break;
                        }

                    }

                }

            }


            // top-right diagonals
            for (int row = 0; row <= 8 - consecutive; row++) {
                for (int col = 0; col <= 8 - consecutive; col++) {
                    if (consecutive == 2) {
                        if (board[row][col] == myColor && board[row + 1][col + 1] == myColor) {
                            totalPoints = totalPoints + 800;
                        } else if (board[row][col] == opponent && board[row + 1][col + 1] == opponent) {
                            totalPoints = totalPoints - 800;
                        }
                    } else if (consecutive == 3) {
                        if (board[row][col] == myColor && board[row + 1][col + 1] == myColor && board[row + 2][col + 2] == myColor) {
                            totalPoints = totalPoints + 240000;
                        } else if (board[row][col] == opponent && board[row + 1][col + 1] == opponent && board[row + 2][col + 2] == opponent) {
                            totalPoints = totalPoints - 240000;
                        }
                    } else if (consecutive == 4) {
                        if (board[row][col] == myColor && myColor == board[row + 1][col + 1] && myColor == board[row + 2][col + 2] && myColor == board[row + 3][col + 3]) {
                            if ((row + 4) < (8 - consecutive) && (col + 4) < (8 - consecutive)) {
                                if (board[row + 4][col + 4] == null) {
                                    if ((row - 1) >= 0 && (col - 1) >= 0) {
                                        if (board[row - 1][col - 1] == null) {
                                            totalPoints = totalPoints + 6000000;
                                        } else {
                                            totalPoints = totalPoints + 800000;
                                        }
                                    } else {
                                        totalPoints = totalPoints + 800000;
                                    }
                                }
                            } else {
                                if ((row - 1) >= 0 && (col - 1) >= 0) {
                                    if (null == board[row - 1][col - 1]) {
                                        totalPoints = totalPoints + 800000;
                                    }
                                }
                            }
                        } else if (board[row][col] == opponent && opponent == board[row + 1][col + 1] && opponent == board[row + 2][col + 2] && opponent == board[row + 3][col + 3]) {
                            if ((row + 4) < (8 - consecutive) && (col + 4) < (8 - consecutive)) {
                                if (board[row + 4][col + 4] == null) {
                                    if ((row - 1) >= 0 && (col - 1) >= 0) {
                                        if (board[row - 1][col - 1] == null) {
                                            totalPoints = totalPoints - 6000000;
                                        } else {
                                            totalPoints = totalPoints - 800000;
                                        }
                                    } else {
                                        totalPoints = totalPoints - 800000;
                                    }
                                }
                            } else {
                                if ((row - 1) >= 0 && (col - 1) >= 0) {
                                    if (null == board[row - 1][col - 1]) {
                                        totalPoints = totalPoints - 800000;
                                    }
                                }
                            }
                        }
                    } else if (consecutive == 5) {
                        if (board[row][col] == myColor && board[row + 1][col + 1] == myColor && board[row + 2][col + 2] == myColor && board[row + 3][col + 3] == myColor && board[row + 4][col + 4] == myColor) {
                            totalPoints = totalPoints + 10000000;
                            // break;
                        } else if (board[row][col] == opponent && board[row + 1][col + 1] == opponent && board[row + 2][col + 2] == opponent && board[row + 3][col + 3] == opponent && board[row + 4][col + 4] == opponent) {
                            totalPoints = totalPoints - 10000000;
                            // break;
                        }
                    }
                }
            }


            // top-left diagonals
            for (int row = 0; row <= 8 - consecutive; row++) {
                for (int col = 7; col >= consecutive - 1; col--) {
                    if (consecutive == 2) {
                        if (myColor == board[row][col] && board[row + 1][col - 1] == myColor) {
                            totalPoints = totalPoints + 800;
                        } else if (opponent == board[row][col] && board[row + 1][col - 1] == opponent) {
                            totalPoints = totalPoints - 800;
                        }

                    } else if (consecutive == 3) {
                        if (myColor == board[row][col] && board[row + 1][col - 1] == myColor && board[row + 2][col - 2] == myColor) {
                            totalPoints = totalPoints + 24000;
                        } else if (opponent == board[row][col] && board[row + 1][col - 1] == opponent && board[row + 2][col - 2] == opponent) {
                            totalPoints = totalPoints - 24000;
                        }

                    } else if (consecutive == 4) {
                        if (myColor == board[row][col] && myColor == board[row + 1][col - 1] && board[row + 2][col - 2] == myColor && board[row + 3][col - 3] == myColor) {
                            if ((row + 4) <= (8 - consecutive) && (col - 4) >= (consecutive - 1)) {
                                if (board[row + 4][col - 4] == null) {
                                    if ((row - 1) >= 0 && (col + 1) <= 7) {
                                        if (board[row - 1][col + 1] == null) {
                                            totalPoints = totalPoints + 6000000;
                                        } else {
                                            totalPoints = totalPoints + 800000;
                                        }
                                    } else {
                                        totalPoints = totalPoints + 800000;
                                    }
                                }
                            } else {
                                if ((row - 1) >= 0 && (col + 1) <= 7) {
                                    if (board[row - 1][col + 1] == null) {
                                        totalPoints = totalPoints + 800000;
                                    }
                                }
                            }
                        } else if (opponent == board[row][col] && opponent == board[row + 1][col - 1] && board[row + 2][col - 2] == opponent && board[row + 3][col - 3] == opponent) {
                            if ((row + 4) <= (8 - consecutive) && (col - 4) >= (consecutive - 1)) {
                                if (board[row + 4][col - 4] == null) {
                                    if ((row - 1) >= 0 && (col + 1) <= 7) {
                                        if (board[row - 1][col + 1] == null) {
                                            totalPoints = totalPoints - 6000000;
                                        } else {
                                            totalPoints = totalPoints - 800000;
                                        }
                                    } else {
                                        totalPoints = totalPoints - 800000;
                                    }
                                }
                            } else {
                                if ((row - 1) >= 0 && (col + 1) <= 7) {
                                    if (board[row - 1][col + 1] == null) {
                                        totalPoints = totalPoints - 800000;
                                    }
                                }
                            }
                        } else if (consecutive == 5) {
                            if (myColor == board[row][col] && board[row + 1][col - 1] == myColor && board[row + 2][col - 2] == myColor && board[row + 3][col - 3] == myColor && board[row + 4][col - 4] == myColor) {
                                totalPoints = totalPoints + 10000000;
                               // break;
                            } else if (opponent == board[row][col] && board[row + 1][col - 1] == opponent && board[row + 2][col - 2] == opponent && board[row + 3][col - 3] == opponent && board[row + 4][col - 4] == opponent) {
                                totalPoints = totalPoints - 10000000;
                               // break;
                            }
                        }
                    }
                }

            }
        } //end evaluateBoard()
        return totalPoints;
    }
    public ArrayList<int[]> legalMoves (Color[][]board, Color myColor){
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        for (int i = 0; i < GomokuBoard.ROWS; i++) {
            for (int j = 0; j < GomokuBoard.COLS; j++) {
                if (board[i][j] == null) {
                    legalMoves.add(new int[]{i, j});
                }
            }
        }

        return legalMoves;
    }

    public boolean hasWon(Color[][] board, Color myColor) {
        Color opponent;
        if (myColor == Color.BLACK)
            opponent = Color.WHITE;
        else
            opponent = Color.BLACK;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == myColor && board[row][col + 1] == myColor && board[row][col + 2] == myColor && board[row][col + 3] == myColor && board[row][col + 4] == myColor)
                    return true;
                if (board[row][col] == opponent && board[row][col + 1] == opponent && board[row][col + 2] == opponent && board[row][col + 3] == opponent && board[row][col + 4] == opponent)
                    return true;
            }
        }

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 4; row++) {

                if (board[row][col] == myColor && board[row + 1][col] == myColor && board[row + 2][col] == myColor && board[row + 3][col] == myColor && board[row + 4][col] == myColor)
                    return true;
                if (board[row][col] == opponent && board[row + 1][col] == opponent && board[row + 2][col] == opponent && board[row + 3][col] == opponent && board[row + 4][col] == opponent)
                    return true;
            }
        }

        for (int start = 0; start <= 3; start++) {
            for (int row = start, col = start; col < 8; row++, col++) {
                if ((col + 4) >= 8)
                    break;
                else {
                    //main diagonal from top left to right
                    if (board[row][col] == myColor && board[row + 1][col + 1] == myColor && board[row + 2][col + 2] == myColor && board[row + 3][col + 3] == myColor && board[row + 4][col + 4] == myColor) {
                        return true;
                    }
                    //opposition
                    if (board[row][col] == opponent && board[row + 1][col + 1] == opponent && board[row + 2][col + 2] == opponent && board[row + 3][col + 3] == opponent && board[row + 4][col + 4] == opponent) {
                        return true;
                    }
                }
            }
        }
        for (int row = 0; row <= 2; row++) {
            for (int col = row + 1; col < 8; col++) {
                if ((col + 4) >= 8)
                    break;
                else {
                    if (board[row][col] == myColor && board[row + 1][col + 1] == myColor && board[row + 2][col + 2] == myColor && board[row + 3][col + 3] == myColor && board[row + 4][col + 4] == myColor) {
                        return true;
                    }
                    if (board[row][col] == opponent && board[row + 1][col + 1] == opponent && board[row + 2][col + 2] == opponent && board[row + 3][col + 3] == opponent && board[row + 4][col + 4] == opponent) {
                        return true;
                    }
                }
            }
        }
        for (int col = 0; col <= 2; col++) {
            for (int row = col + 1; row < 8; row++) {
                if ((row + 4) >= 8)
                    break;
                else {
                    if (board[row][col] == myColor && board[row + 1][col + 1] == myColor && board[row + 2][col + 2] == myColor && board[row + 3][col + 3] == myColor && board[row + 4][col + 4] == myColor) {
                        return true;
                    }
                    if (board[row][col] == opponent && board[row + 1][col + 1] == opponent && board[row + 2][col + 2] == opponent && board[row + 3][col + 3] == opponent && board[row + 4][col + 4] == opponent) {
                        return true;
                    }
                }
            }
        }

        int consecutive = 5;
        for (int row = 0; row <= 8 - consecutive; row++) {
            for (int col = 7; col >= consecutive - 1; col--) { //start pos
                if (myColor == board[row][col] && myColor == board[row + 1][col - 1] && myColor == board[row + 2][col - 2] && myColor == board[row + 3][col - 3] && myColor == board[row + 4][col - 4]) {
                    return true;
                } else if (opponent == board[row][col] && opponent == board[row + 1][col - 1] && opponent == board[row + 2][col - 2] && opponent == board[row + 3][col - 3] && opponent == board[row + 4][col - 4]) {
                    return true;
                }
            }
        }
        return false;
    }

}// end GomokuPlayerAi
