package rps.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import rps.exception.GameNotFoundException;
import rps.exception.StorageException;
import rps.model.GameState;
import rps.model.Option;
import rps.model.dto.TurnRequest;
import rps.service.GameService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {
    @Mock
    private GameService gameService;
    @InjectMocks
    private GameController gameController;

    @Test
    void successfulStartTest() throws Exception {
        var gameId = "game_id_123";
        doReturn(gameId).when(gameService).startGame();

        var response = gameController.startGame();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(gameId, response.getBody().getGameId());
    }

    @Test
    void serverErrorStartTest() throws Exception {
        doThrow(StorageException.class).when(gameService).startGame();

        var response = gameController.startGame();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void successfulTurnTest() throws Exception {
        var gameId = "game_id_123";
        var playerTurn = Option.SCISSORS;
        var computerTurn = Option.ROCK;
        doReturn(computerTurn).when(gameService).getComputerTurn(gameId);
        doReturn("some string").when(gameService).playGameRound(gameId, playerTurn, computerTurn);

        var response = gameController.makeTurn(gameId, new TurnRequest(playerTurn.toString()));

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void wrongInputTurnTest() throws Exception {
        var gameId = "game_id_123";

        var response = gameController.makeTurn(gameId, new TurnRequest("bad_string"));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void serverErrorTurnTest() throws Exception {
        var gameId = "game_id_123";
        var playerTurn = Option.SCISSORS;
        doThrow(StorageException.class).when(gameService).getComputerTurn(gameId);

        var response = gameController.makeTurn(gameId, new TurnRequest(playerTurn.toString()));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void gameNotFoundTurnTest() throws Exception {
        var gameId = "game_id_123";
        var playerTurn = Option.SCISSORS;
        doThrow(GameNotFoundException.class).when(gameService).getComputerTurn(gameId);

        var response = gameController.makeTurn(gameId, new TurnRequest(playerTurn.toString()));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void successfulGetStatisticsTest() throws Exception {
        var gameId = "game_id_123";
        doReturn(new GameState()).when(gameService).getGameState(gameId);

        var response = gameController.getStatistics(gameId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void serverErrorGetStatisticsTest() throws Exception {
        var gameId = "game_id_123";
        doThrow(StorageException.class).when(gameService).getGameState(gameId);

        var response = gameController.getStatistics(gameId);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void gameNotFoundGetStatisticsTest() throws Exception {
        var gameId = "game_id_123";
        doThrow(GameNotFoundException.class).when(gameService).getGameState(gameId);

        var response = gameController.getStatistics(gameId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void successfulDeleteTest() throws Exception {
        var gameId = "game_id_123";
        doNothing().when(gameService).deleteGame(gameId);

        var response = gameController.deleteGame(gameId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void serverErrorDeleteTest() throws Exception {
        var gameId = "game_id_123";
        doThrow(StorageException.class).when(gameService).deleteGame(gameId);

        var response = gameController.deleteGame(gameId);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void gameNotFoundDeleteTest() throws Exception {
        var gameId = "game_id_123";
        doThrow(GameNotFoundException.class).when(gameService).deleteGame(gameId);

        var response = gameController.deleteGame(gameId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
