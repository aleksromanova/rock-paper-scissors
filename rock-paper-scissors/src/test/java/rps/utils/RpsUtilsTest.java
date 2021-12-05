package rps.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rps.model.GameHistory;

import static rps.model.Option.ROCK;
import static rps.model.Option.PAPER;
import static rps.model.Option.SCISSORS;
import static rps.model.RoundResult.DRAW;
import static rps.model.RoundResult.WON;
import static rps.model.RoundResult.LOST;

class RpsUtilsTest {
    @Test
    void generateComputerTurnTest() {
        var history = new GameHistory();
        history.setLastPlayerTurn(ROCK);
        int[][] probabilityMatrix = {{3, 2, 2}, {1, 0, 1}, {0, 1, 1}};
        history.setProbabilityMatrix(probabilityMatrix);

        var computerTurn = RpsUtils.generateComputerTurn(history);
        Assertions.assertEquals(PAPER, computerTurn, "Result should fit probability matrix");
    }

    @Test
    void calculateRoundResultTest() {
        //test all possible combinations of options
        Assertions.assertEquals(DRAW, RpsUtils.calculateRoundResult(ROCK, ROCK));
        Assertions.assertEquals(DRAW, RpsUtils.calculateRoundResult(SCISSORS, SCISSORS));
        Assertions.assertEquals(DRAW, RpsUtils.calculateRoundResult(PAPER, PAPER));
        Assertions.assertEquals(WON, RpsUtils.calculateRoundResult(ROCK, SCISSORS));
        Assertions.assertEquals(WON, RpsUtils.calculateRoundResult(PAPER, ROCK));
        Assertions.assertEquals(WON, RpsUtils.calculateRoundResult(SCISSORS, PAPER));
        Assertions.assertEquals(LOST, RpsUtils.calculateRoundResult(SCISSORS, ROCK));
        Assertions.assertEquals(LOST, RpsUtils.calculateRoundResult(ROCK, PAPER));
        Assertions.assertEquals(LOST, RpsUtils.calculateRoundResult(PAPER, SCISSORS));
    }
}
