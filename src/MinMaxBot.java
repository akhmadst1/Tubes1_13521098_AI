import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinMaxBot extends Bot {
    public boolean hitTimeout = false;
    public static String MAX = "O";
    public static String MIN = "X";

    public int[] move() {
        String[][] state = stateCopy();
        int[] result = new int[2];
        long currentTime = System.currentTimeMillis();
        long timeout = currentTime + 4000;

        List<int[]> moves = getAllPossibleMoves(state);

        for (int i = 1; i <= 4; i++) {
            if (System.currentTimeMillis() > timeout) {
                break;
            }
            Object[] action = abpruning(state, i, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, timeout, MAX).get(0);
            if (!hitTimeout && action[1] != null) {
                int[] temp = (int[]) action[1];
                result[0] = temp[0];
                result[1] = temp[1];
            } else {
                int[] fallback = moves.get(0);
                result[0] = fallback[0];
                result[1] = fallback[1];
            }
        }
        // create move
        return new int[]{result[0], result[1]};
    }

    public double evaluate(String[][] board) {
        double score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals("O")){
                    score += 1;
                }
            }
        }
        return score;
    }

    public List<Object[]> abpruning(String[][] board, int depth, double alpha, double beta, long timeout, String turn) {
        List<Object[]> result = new ArrayList<>();

        if (System.currentTimeMillis() > timeout) {
            this.hitTimeout = true;
            double util_value = evaluate(board);
            Object[] entry = {util_value, null};
            result.add(entry);
            return result;
        }
        if (depth == 0){
            double util_value = evaluate(board);
            Object[] entry = {util_value, null};
            result.add(entry);
            return result;
        }

        List<int[]> allMoves = getAllPossibleMoves(board);

        if (MAX.equals(turn)) {
            double maxEval = Double.NEGATIVE_INFINITY;
            int[] bestMaxMove = allMoves.get(0);
            double bestMaxScore = maxEval;

            for (int[] move : allMoves) {
                String[][] currentMax = copy(board);
                String[][] appliedBoardMax = applyAction(currentMax, move, MAX);

                Object[] entry = abpruning(appliedBoardMax, depth - 1, alpha, beta, timeout, MIN).get(0);
                maxEval = Math.max(maxEval, (double)entry[0]);

                if (maxEval > bestMaxScore) {
                    bestMaxScore = maxEval;
                    bestMaxMove = move;
                }

                alpha = Math.max(alpha, maxEval);

                if (beta <= alpha) {
                    break;
                }
            }

            Object[] entry = {bestMaxScore, bestMaxMove};
            result.add(entry);
            return result;

        } else {
            double minEval = Double.POSITIVE_INFINITY;
            int[] bestMinMove = allMoves.get(0);
            double bestMinScore = minEval;

            for (int[] move : allMoves) {
                String[][] currentMin = copy(board);
                String[][] appliedBoardMin = applyAction(currentMin, move, MIN);

                Object[] entry = abpruning(appliedBoardMin, depth - 1, alpha, beta, timeout, MAX).get(0);
                minEval = Math.min(minEval, (double)entry[0]);

                System.out.println(Arrays.toString(move));
                System.out.println(minEval);

                if (minEval < bestMinScore) {
                    bestMinScore = minEval;
                    bestMinMove = move;
                }

                beta = Math.min(beta, minEval);

                if (beta <= alpha) {
                    break;
                }
            }
            Object[] entry = {bestMinScore, bestMinMove};
            result.add(entry);
            return result;
        }
    }

    public String[][] applyAction(String[][] board, int[] coordinate, String turn){
        int x = coordinate[0];
        int y = coordinate[1];
        String[][] newboard = copy(board);
        newboard[x][y] = turn;

        int startRow, endRow, startColumn, endColumn;

        if (x - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = x;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = x - 1;

        if (x + 1 >= 8)  // If clicked button in last row, no subsequent/further row exists.
            endRow = x;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = x + 1;

        if (y - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = y;
        else
            startColumn = y - 1;

        if (y + 1 >= 8)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = y;
        else
            endColumn = y + 1;


        // Search for adjacency for X's and O's or vice versa, and replace them.
        for (int i = startRow; i <= endRow; i++) {
            if (!newboard[i][y].equals(""))
                newboard[i][y] = turn;
        }
        for (int j = startColumn; j <= endColumn; j++) {
            if (!newboard[x][j].equals(""))
                newboard[x][j] = turn;
        }
        return newboard;
    }
}