package rps.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import rps.exception.GameNotFoundException;
import rps.exception.StorageException;
import rps.model.GameHistory;
import rps.model.GameState;
import rps.model.Option;
import rps.repository.GamesRepository;
import rps.utils.RpsUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GamesRepository repository;
    @InjectMocks
    private GameService gameService;

    @Test
    void startGameSuccessTest() throws Exception {
        doNothing().when(repository).saveGame(anyString(), any(GameState.class));

        var gameId = gameService.startGame();

        verify(repository).saveGame(eq(gameId), any(GameState.class));
        Assertions.assertNotNull(gameId, "GameId must be not null");
        Assertions.assertFalse(gameId.isEmpty(), "GameId must be not empty");
    }

    @Test()
    void startGameDbErrorTest() throws Exception {
        doThrow(StorageException.class).when(repository).saveGame(anyString(), any(GameState.class));

        assertThrows(StorageException.class, gameService::startGame);
    }

    @Test
    void getComputerTurnSuccessTest() throws Exception {
        try (MockedStatic<RpsUtils> utilities = mockStatic(RpsUtils.class)) {
            utilities.when(() -> RpsUtils.generateComputerTurn(any(GameHistory.class)))
                    .thenReturn(Option.ROCK);
            var gameId = "game_id_1";
            doReturn(new GameState()).when(repository).getGame(gameId);

            var computerTurn = gameService.getComputerTurn(gameId);

            verify(repository).getGame(gameId);
            Assertions.assertEquals(Option.ROCK, computerTurn);
        }
    }

    @Test
    void getComputerTurnGameNotExistsTest() throws Exception {
        var gameId = "game_id_1";
        doReturn(null).when(repository).getGame(gameId);

        assertThrows(GameNotFoundException.class, () -> gameService.getComputerTurn(gameId));
    }

    @Test()
    void getComputerTurnDbErrorTest() throws Exception {
        var gameId = "game_id_1";
        doThrow(StorageException.class).when(repository).getGame(gameId);

        assertThrows(StorageException.class, () -> gameService.getComputerTurn(gameId));
    }

    @Test
    void playGameRoundSuccessTest() throws Exception {
        var gameId = "game_id_1";
        var gameState = new GameState();
        doReturn(gameState).when(repository).getGame(gameId);
        doNothing().when(repository).saveGame(gameId, gameState);

        var roundResult = gameService.playGameRound(gameId, Option.ROCK, Option.ROCK);

        verify(repository).getGame(gameId);
        verify(repository).saveGame(eq(gameId), any(GameState.class));
        Assertions.assertEquals("draw", roundResult);
    }

    @Test
    void playGameRoundGameNotExistsTest() throws Exception {
        var gameId = "game_id_1";
        doReturn(null).when(repository).getGame(gameId);

        assertThrows(GameNotFoundException.class, () -> gameService.playGameRound(gameId, Option.ROCK, Option.ROCK));
    }

    @Test
    void playGameRoundDbErrorTest() throws Exception {
        var gameId = "game_id_1";
        doThrow(StorageException.class).when(repository).getGame(gameId);

        assertThrows(StorageException.class, () -> gameService.playGameRound(gameId, Option.ROCK, Option.ROCK));
    }

    @Test
    void getGameStateSuccessTest() throws Exception {
        var gameId = "game_id_1";
        var expectedGameState = new GameState();
        doReturn(expectedGameState).when(repository).getGame(gameId);

        var gameState = gameService.getGameState(gameId);

        verify(repository).getGame(gameId);
        Assertions.assertEquals(expectedGameState, gameState);
    }

    @Test
    void getGameStateGameNotExistsTest() throws Exception {
        var gameId = "game_id_1";
        doReturn(null).when(repository).getGame(gameId);

        assertThrows(GameNotFoundException.class, () -> gameService.getGameState(gameId));
    }

    @Test
    void getGameStateDbErrorTest() throws Exception {
        var gameId = "game_id_1";
        doThrow(StorageException.class).when(repository).getGame(gameId);

        assertThrows(StorageException.class, () -> gameService.getGameState(gameId));
    }

    @Test
    void deleteGameSuccessTest() throws Exception {
        var gameId = "game_id_1";
        doReturn(true).when(repository).deleteGame(gameId);

        gameService.deleteGame(gameId);

        verify(repository).deleteGame(gameId);
    }

    @Test
    void deleteGameGameNotExistsTest() throws Exception {
        var gameId = "game_id_1";
        doReturn(false).when(repository).deleteGame(gameId);

        assertThrows(GameNotFoundException.class, () -> gameService.deleteGame(gameId));
    }

    @Test
    void deleteGameDbErrorTest() throws Exception {
        var gameId = "game_id_1";
        doThrow(StorageException.class).when(repository).deleteGame(gameId);

        assertThrows(StorageException.class, () -> gameService.deleteGame(gameId));
    }
}
