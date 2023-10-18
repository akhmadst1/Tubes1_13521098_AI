import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Bot {
    public abstract int[] move();

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

    public List<int[]> getAllPossibleMoves(String[][] board){
        List<int[]> coordinatesList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals("")){
                    int[] coordinate = {i, j};
                    coordinatesList.add(coordinate);
                }
            }
        }
        Collections.shuffle(coordinatesList);
        return coordinatesList;
    }
}

