package rps.repository;

import rps.model.GameState;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryGamesRepository implements GamesRepository {
    private Map<String, GameState> storage = new HashMap<>();

    @Override
    public void saveGame(String gameId, GameState gameState) {
        storage.put(gameId, gameState);
    }

    @Override
    public GameState getGame(String gameId) {
        return storage.get(gameId);
    }

    @Override
    public boolean deleteGame(String gameId) {
        return (storage.remove(gameId) != null);
    }
}
