package rps.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class GameHistory {
    private Option lastPlayerTurn;
    private int[][] probabilityMatrix;

    public GameHistory() {
        int length = Option.values().length;
        probabilityMatrix = new int[length][length];
        Arrays.stream(probabilityMatrix).forEach(row -> Arrays.fill(row, 0));
    }

    public void updateData(Option playerTurn) {
        if (lastPlayerTurn != null) {
            probabilityMatrix[lastPlayerTurn.ordinal()][playerTurn.ordinal()]++;
        }
        lastPlayerTurn = playerTurn;
    }
}
