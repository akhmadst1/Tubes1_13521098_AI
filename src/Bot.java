public class Bot {
//    public OutputFrameController state = new OutputFrameController();

    public int[] move() {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

    public int evaluate() {
        int score;
        if (OutputFrameController.playerXTurn){
            score = OutputFrameController.playerXScore;
            if (OutputFrameController.roundsLeft == 0) {
                if (score > OutputFrameController.playerOScore) {
                    score += 100;
                } else {
                    score -= 100;
                }
            }
        } else{
            score = OutputFrameController.playerOScore;
            if (OutputFrameController.roundsLeft == 0) {
                if (score > OutputFrameController.playerXScore) {
                    score += 100;
                } else {
                    score -= 100;
                }
            }
        }

        int scorePredict = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

                if (OutputFrameController.buttons[i][j].getText().equals("")){
                    for (int x = startRow; x <= endRow; x++) {
                        scorePredict += checkPredict(x, j);
                    }
                    for (int y = startColumn; y <= endColumn; y++) {
                        scorePredict += checkPredict(i, y);
                    }
                }
            }
        }
        return score + scorePredict;
    }

    private int checkPredict(int i, int j){
        int score = 0;
        if (OutputFrameController.playerXTurn) {
            if (OutputFrameController.buttons[i][j].getText().equals("O")) {
                score++;
            }
        } else if (OutputFrameController.buttons[i][j].getText().equals("X")) {
            score++;
        }
        return score;
    }
}
