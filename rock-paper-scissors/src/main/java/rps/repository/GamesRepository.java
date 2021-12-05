package rps.repository;

import rps.exception.StorageException;
import rps.model.GameState;

/**
 * Storage for game statistics
 */
public interface GamesRepository {

    /**
     * Save game statistics to storage
     *
     * @param gameState
     */
    void saveGame(String gameId, GameState gameState) throws StorageException;

    /**
     * Get game from storage by the gameId
     *
     * @param gameId id of the game
     * @return Statistics of the game
     */
    GameState getGame(String gameId) throws StorageException;

    boolean deleteGame(String gameId) throws StorageException;
}
