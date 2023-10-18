import java.util.*;

public class Bot {
    public boolean hitTimeout;
    public static boolean isAgentTurn;

    public int[] move() {
        hitTimeout = false;
        long currentTime = System.currentTimeMillis();
        long timeout = currentTime + 4000;
        List<int[]> moves = getAllPossibleMoves();
        String[][] state = stateCopy();
        int[] result = new int[2];
        for (int i = 1; i <= moves.size(); i++) {
            if (System.currentTimeMillis() > timeout) {
                break;
            }
            isAgentTurn = true;
            Object[] action = abpruning(state, 3, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, isAgentTurn, timeout).get(0);
//            System.out.println(i + " . " + action[0]);

            if (!hitTimeout && action[1] != null) {
                int[] temp = (int[]) action[1];
                result[0] = temp[0];
                result[1] = temp[1];
            }
        }

        // create move
        return new int[]{result[0], result[1]};
    }

    public String[][] stateCopy(){
        String[][] board = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = OutputFrameController.buttons[i][j].getText();
            }
        }
        return board;
    }

    public String[][] copy(String[][] board){
        String[][] newboard = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        return newboard;
    }

    public List<int[]> getAllPossibleMoves(){
        List<int[]> coordinatesList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (OutputFrameController.buttons[i][j].getText().equals("")){
                    int[] coordinate = {i, j};
                    coordinatesList.add(coordinate);
                }
            }
        }
        Collections.shuffle(coordinatesList);
        return coordinatesList;
    }

    public double evaluate(String[][] board) {
        double score;
        if (OutputFrameController.playerXTurn) {
            score = 20 * (OutputFrameController.playerXScore - OutputFrameController.playerOScore);
        } else {
            score = 20 * (OutputFrameController.playerOScore - OutputFrameController.playerXScore);
        }

        double scorePredict = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals("")){
                    // CHECKING CONDITION WHERE BOT DON'T WANT TO PUT TO CERTAIN BOX

                    if (i - 2 < 0) { // First-to second row, not checking above
                        if (j - 2 < 0) {    // First-to-second column, not checking left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else if (j + 2 >= 8){ // seventh-to-eight column, not checking right
                            if (OutputFrameController.playerXTurn) {
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else { // third-to sixth column, checking right and left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        }
                    } else if (i + 2 >= 8) { // seventh-to-eight row, not checking below
                        if (j - 2 < 0) {    // First-to-second column, not checking left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else if (j + 2 >= 8){ // seventh-to-eight column, not checking right
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else { // third-to sixth column, checking right and left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        }
                    } else { // third-to-sixth row, checking above and below
                        if (j - 2 < 0) {    // First-to-second column, not checking left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else if (j + 2 >= 8){ // seventh-to-eight column, not checking right
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        } else { // third-to sixth column, checking right and left
                            if (OutputFrameController.playerXTurn) {
                                if (board[i-2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("X")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("X")) {
                                    scorePredict -= 5;
                                }
                            } else {
                                if (board[i-2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i+2][j].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j-2].equals("O")) {
                                    scorePredict -= 5;
                                }
                                if (board[i][j+2].equals("O")) {
                                    scorePredict -= 5;
                                }
                            }
                        }
                    }

                    // CHECKING CONDITION WHERE BOT WANT TO PUT TO THE BOX

                    int startRow, endRow, startColumn, endColumn;

                    if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
                        startRow = i;
                    else               // Otherwise, the preceding row exists for adjacency.
                        startRow = i - 1;

                    if (i + 1 >= 8)  // If clicked button in last row, no subsequent/further row exists.
                        endRow = i;
                    else               // Otherwise, the subsequent row exists for adjacency.
                        endRow = i + 1;

                    if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
                        startColumn = j;
                    else
                        startColumn = j - 1;

                    if (j + 1 >= 8)  // If clicked on last column, upper bound of the column has been reached.
                        endColumn = j;
                    else
                        endColumn = j + 1;

                    // Search for adjacency for X's and O's or vice versa, and check them
                    for (int x = startRow; x <= endRow; x++) {
                        scorePredict += 10*checkPredict(x, j);
                    }

                    for (int y = startColumn; y <= endColumn; y++) {
                        scorePredict += 10*checkPredict(i, y);
                    }
                }
            }
        }
//        System.out.println("score " + score);
//        System.out.println("scorepredict " + scorePredict);
        return score + scorePredict;
    }

    private double checkPredict(int i, int j){
        double score = 0;
        if (OutputFrameController.playerXTurn) {
            if (OutputFrameController.buttons[i][j].getText().equals("O")) {
                score++;
            }
        } else {
            if (OutputFrameController.buttons[i][j].getText().equals("X")) {
                score++;
            }
        }
        return score;
    }

    public List<Object[]> abpruning(String[][] board, int depth, double alpha, double beta, boolean agentTurn, long timeout) {
        if (System.currentTimeMillis() > timeout) {
            this.hitTimeout = true;
            List<Object[]> result = new ArrayList<>();
            double util_value = evaluate(board);
            int[] noresult = {-1,-1};
            Object[] entry = {util_value, noresult};
            result.add(entry);
            return result;
        }
        if (depth == 0 || OutputFrameController.roundsLeft == 0) {
            List<Object[]> result = new ArrayList<>();
            double util_value = evaluate(board);
            int[] noresult = {-1,-1};
            Object[] entry = {util_value, noresult};
            result.add(entry);
            return result;
        }

        List<int[]> allMoves = getAllPossibleMoves();
        System.out.println(depth);

        if (agentTurn) {
            double maxEval = Double.NEGATIVE_INFINITY;
            int[] bestMove = allMoves.get(0);
            double bestScore = maxEval;

            for (int[] move : allMoves) {
                String[][] current = copy(board);
                current = applyAction(current, move);
//                System.out.println(Arrays.deepToString(current));

                isAgentTurn = false;
                Object[] entry = abpruning(current, depth - 1, alpha, beta, isAgentTurn, timeout).get(0);
                maxEval = Math.max(maxEval, (double)entry[0]);

                if (maxEval > bestScore) {
                    bestScore = maxEval;
                    bestMove = move;
                }

                alpha = Math.max(alpha, maxEval);

                if (beta <= alpha) {
//                    System.out.println("pruning max");
                    break;
                }
            }
//            System.out.println("max " + bestScore);

            List<Object[]> result = new ArrayList<>();
            Object[] entry = {bestScore, bestMove};
            result.add(entry);
            return result;

        } else {
            double minEval = Double.POSITIVE_INFINITY;
            int[] bestMove = allMoves.get(0);
            double bestScore = minEval;

            for (int[] move : allMoves) {
                String[][] current = copy(board);
                current = applyAction(current, move);
//                System.out.println(Arrays.deepToString(current));

                isAgentTurn = true;
                Object[] entry = abpruning(current, depth - 1, alpha, beta, isAgentTurn, timeout).get(0);
                minEval = Math.min(minEval, (double)entry[0]);

                if (minEval < bestScore) {
                    bestScore = minEval;
                    bestMove = move;
                }

                beta = Math.min(beta, minEval);

                if (beta <= alpha) {
//                    System.out.println("pruning min");
                    break;
                }
            }
//            System.out.println("min " + bestScore);

            List<Object[]> result = new ArrayList<>();
            Object[] entry = {bestScore, bestMove};
            result.add(entry);
            return result;
        }
    }

    public String[][] applyAction(String[][] board, int[] coordinate){
        int x = coordinate[0];
        int y = coordinate[1];
        String[][] newboard = copy(board);
        if (OutputFrameController.playerXTurn) {
            newboard[x][y] = "X";
        } else {
            newboard[x][y] = "O";
        }
        return newboard;
    }
}
