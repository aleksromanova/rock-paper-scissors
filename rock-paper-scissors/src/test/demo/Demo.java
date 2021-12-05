import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import rps.model.dto.StartResponse;
import rps.model.dto.TurnRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.CompletionException;

/**
 * Console app to show how application works
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to rock-paper-scissors game!");
        var scanner = new Scanner(System.in);
        String gameId = startGame();
        printMenu();
        while (true) {
            var command = scanner.nextInt();
            switch (command) {
                case 1:
                    makeTurn(gameId);
                    break;
                case 2:
                    printStatistics(gameId);
                    break;
                case 3:
                    deleteGame(gameId);
                    return;
                default:
                    System.out.println("Wrong command!");
                    break;
            }
            printMenu();
        }
    }

    private static String startGame() throws Exception {
        var uncheckedObjectMapper = new StartResponseMapper();
        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080/api/rps/start"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        var client = HttpClient.newHttpClient();
        var startResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(uncheckedObjectMapper::readValue)
                .get();
        var gameId = startResponse.getGameId();
        System.out.println("Game Id: " + gameId);
        return gameId;
    }

    private static void makeTurn(String gameId) throws Exception {
        if (gameId.isEmpty()) {
            System.out.println("You have to start a game first!");
            return;
        }
        System.out.println("Enter your turn rock, paper or scissors");
        var scanner = new Scanner(System.in);
        var playerOption = scanner.next();
        var requestObject = new TurnRequest(playerOption);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(requestObject);
        var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/api/rps/" + gameId + "/turn"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        var client = HttpClient.newHttpClient();
        var turnResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status code: " + turnResponse.statusCode());
        if (!turnResponse.body().isEmpty()) {
            System.out.println(turnResponse.body());
        }
    }

    private static void printStatistics(String gameId) throws Exception {
        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080/api/rps/" + gameId))
                .build();
        var client = HttpClient.newHttpClient();
        var statisticsResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(statisticsResponse.body());
    }

    private static void deleteGame(String gameId) throws Exception {
        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:8080/api/rps/" + gameId))
                .DELETE()
                .build();
        var client = HttpClient.newHttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Please enter number of desired action:");
        System.out.println("To make turn - 1");
        System.out.println("To print statistics - 2");
        System.out.println("To exit - 3");
    }
}

class StartResponseMapper extends com.fasterxml.jackson.databind.ObjectMapper {
    StartResponse readValue(String content) {
        try {
            return this.readValue(content, new TypeReference<>() {
            });
        } catch (IOException ioe) {
            throw new CompletionException(ioe);
        }
    }
}