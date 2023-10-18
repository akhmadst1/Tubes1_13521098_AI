import java.util.ArrayList;
import java.util.List;

public class LocalSearchBot extends Bot {
    private String myChar = " ";

    public LocalSearchBot(String turn) {
        this.myChar = turn;
    }

    public int[] move() {
        String[][] state = stateCopy();
        List<int[]> result = hillClimbingSideWaysMoves(state);
        int[] chosen = result.get(0);
        return new int[]{chosen[0], chosen[1]};
    }

    public List<int[]> hillClimbingSideWaysMoves(String[][] board) {
        List<int[]> result = new ArrayList<>();
        List<int[]> moves = getAllPossibleMoves(board);
        int maxScore = 0;
        int score = 1;

        for (int[] move : moves) {
            int startRow, endRow, startColumn, endColumn;

            if (move[0] - 1 < 0)     // If clicked button in first row, no preceding row exists.
                startRow = move[0];
            else               // Otherwise, the preceding row exists for adjacency.
                startRow = move[0] - 1;

            if (move[0] + 1 >= 8)  // If clicked button in last row, no subsequent/further row exists.
                endRow = move[0];
            else               // Otherwise, the subsequent row exists for adjacency.
                endRow = move[0] + 1;

            if (move[1] - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
                startColumn = move[1];
            else
                startColumn = move[1] - 1;

            if (move[1] + 1 >= 8)  // If clicked on last column, upper bound of the column has been reached.
                endColumn = move[1];
            else
                endColumn = move[1] + 1;

            // Search for adjacency for X's and O's or vice versa
            for (int x = startRow; x <= endRow; x++) {
                if (this.myChar.equals("X")){
                    if (board[x][move[1]].equals("O"))
                        score++;
                } else{
                    if (board[x][move[1]].equals("X"))
                        score++;
                }
            }
            for (int y = startColumn; y <= endColumn; y++) {
                if (this.myChar.equals("X")){
                    if (board[move[0]][y].equals("O"))
                        score++;
                } else {
                    if (board[move[0]][y].equals("X"))
                        score++;
                }
            }
            if (score > maxScore) {
                result.clear();
                maxScore = score;
                result.add(move);
            } else if (score == maxScore) {
                result.add(move);
            }
        }
        return result;
    }
}