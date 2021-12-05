package rps.controller;

import rps.exception.GameNotFoundException;
import rps.exception.StorageException;
import rps.model.Option;
import rps.model.dto.StartResponse;
import rps.model.dto.StatisticsResponse;
import rps.model.dto.TurnRequest;
import rps.model.dto.TurnResponse;
import rps.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RPS game API
 */
@RestController
@RequestMapping("/api/rps")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * POST method to start new game
     *
     * @return response entity with gameId or error code
     */
    @PostMapping("/start")
    public ResponseEntity<StartResponse> startGame() {
        try {
            return ResponseEntity.ok().body(new StartResponse(gameService.startGame()));
        } catch (StorageException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * POST method to make a turn
     *
     * @param gameId id of the game
     * @param turn player's turn
     * @return response entity for TurnResponse or error code
     */
    @PostMapping("/{gameId}/turn")
    public ResponseEntity<TurnResponse> makeTurn(@PathVariable String gameId, @RequestBody TurnRequest turn) {
        try {
            var playerTurn = Option.valueOf(turn.getPlayerTurn().toUpperCase());
            var computerTurn = gameService.getComputerTurn(gameId);
            var roundResult = gameService.playGameRound(gameId, playerTurn, computerTurn);
            return ResponseEntity.ok().body(new TurnResponse(computerTurn.toString().toLowerCase(), roundResult));
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (StorageException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * GET method to recieve game statistics
     *
     * @param gameId id of the game
     * @return response entity for StatisticsResponse or error code
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<StatisticsResponse> getStatistics(@PathVariable String gameId) {
        try {
            var gameState = gameService.getGameState(gameId);
            var response = new StatisticsResponse(gameState.getWon(), gameState.getLost(), gameState.getDraw());
            return ResponseEntity.ok().body(response);
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (StorageException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * DELETE method for RPS game
     *
     * @param gameId id of the game
     * @return http status for result
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity deleteGame(@PathVariable String gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.ok().build();
        } catch (GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (StorageException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
