package rps.model;

import lombok.Getter;
import rps.utils.RpsUtils;

import java.util.EnumMap;
import java.util.Map;

public class GameState {
    private Map<RoundResult, Integer> statistics = new EnumMap<>(RoundResult.class);
    @Getter
    private GameHistory gameHistory;

    public GameState() {
        for (RoundResult result : RoundResult.values()) {
            statistics.put(result, 0);
        }
        gameHistory = new GameHistory();
    }

    public RoundResult playRound(Option playerTurn, Option computerTurn) {
        gameHistory.updateData(playerTurn);
        var roundResult = RpsUtils.calculateRoundResult(playerTurn, computerTurn);
        statistics.merge(roundResult, 1, Integer::sum);
        return roundResult;
    }

    public int getWon() {
        return statistics.get(RoundResult.WON);
    }

    public int getLost() {
        return statistics.get(RoundResult.LOST);
    }

    public int getDraw() {
        return statistics.get(RoundResult.DRAW);
    }
}
