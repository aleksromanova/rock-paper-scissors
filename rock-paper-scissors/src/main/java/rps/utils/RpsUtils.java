package rps.utils;

import rps.model.GameHistory;
import rps.model.Option;
import rps.model.OptionComparator;
import rps.model.RoundResult;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Static methods for RPS game
 */
public class RpsUtils {
    private RpsUtils() {
    }

    /**
     * Generate computer turn based on previous turn
     *
     * @param gameHistory information about previous player's turns
     * @return computer turn
     */
    public static Option generateComputerTurn(GameHistory gameHistory) {
        var lastPlayerTurn = gameHistory.getLastPlayerTurn();
        if (gameHistory.getLastPlayerTurn() == null) {
            //if it's a fist turn return random option
            return Option.values()[new Random().nextInt(Option.values().length)];
        } else {
            //trying to predict which option would be next and return option which would beat it
            int[] optionRow = gameHistory.getProbabilityMatrix()[lastPlayerTurn.ordinal()];
            var nextIndex =  IntStream.range(0, optionRow.length)
                    .reduce((i, j) -> optionRow[i] > optionRow[j] ? i : j)
                    .orElse(0);
            var predictedNext = Option.values()[nextIndex];
            return predictedNext.getKiller();
        }
    }

    /**
     * Calculate result of RPS game round
     *
     * @param playerTurn   player's choice
     * @param computerTurn computer's choice
     * @return round result
     */
    public static RoundResult calculateRoundResult(Option playerTurn, Option computerTurn) {
        var roundResult = (new OptionComparator()).compare(playerTurn, computerTurn);
        if (roundResult < 0) {
            return RoundResult.LOST;
        }
        if (roundResult > 0) {
            return RoundResult.WON;
        }
        return RoundResult.DRAW;
    }
}
