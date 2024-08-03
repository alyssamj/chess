package ui;

import com.sun.source.tree.BreakTree;
import requestsandresponses.*;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {

    private final String serverUrl;
    private final ServerFacade server;
    private String authToken;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String evalPreLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> helpLogin();
      //          case "quit" -> quit();
                case "login" -> login(params);
                case "register" -> register(params);
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public String evalPostLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> helpPost();
                case "logout" -> logout();
          //      case "quit" -> quit();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
            };
        } catch (Exception e) {
            throw new RuntimeException("Unable to get command");
        }
    }

    private String helpPost() {
        return """
                create <GAMENAME> - create a new game
                list - list games
                join <ID> [WHITE|BLACK] - join a game as white or black
                observe <ID> - watch a game
                logout - logout when finished
                quit - stop playing chess
                help - possbile commands
                """;
    }

    public String helpLogin() {

        return """
        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
        login <USERNAME> <PASSWORD> - to play chess
        quit - stop playing chess
        help - possible commands
        """;
    }

    public String logout() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResult logoutResult = server.logout(logoutRequest);
        if (logoutResult.message() != null) {
            return logoutResult.message();
        }
        return "logged out";
    }

    public String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = server.register(registerRequest);
        authToken = registerResult.authToken();
        if (authToken != null) {
            String[] loginParams = {params[0], params[1]};
            this.login(loginParams);
        }
        return "registered - press help";
    }

    public String login(String[] params) {
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = server.login(loginRequest);
        return "login - press help";
    }

    public String createGame(String[] params) {
        String gameName = params[0];
        CreateRequest createRequest = new CreateRequest(authToken, gameName);
        CreateResult createResult = server.createGame(createRequest);
        return String.valueOf(createResult.gameID());
    }

    public String listGames(String[] params) {
        ListRequest listRequest = new ListRequest(authToken);
        ListResult listResult = server.listGames(listRequest);
        StringBuilder stringBuilder = new StringBuilder();
        int gameNumber = 1;
        for (ArrayListResult game : listResult.games()) {
            int gameID = game.gameID();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            String gameName = game.gameName();
            String gameFull;
            if (blackUsername != null && whiteUsername != null) {
                gameFull = "Full Game";
            } else if (blackUsername != null && whiteUsername == null)  {
                gameFull = "White Player Available";
            } else if (blackUsername == null && whiteUsername != null) {
                gameFull = "Black Player Available";
            } else {
                gameFull = "Either Color Available";
            }
            String stringToAdd = String.format("""
                    %d: %s  - %s
                        White Player: %s  
                        Black Player: %s
                  
                    """, gameNumber, gameName, gameFull, whiteUsername, blackUsername);
           stringBuilder.append(stringToAdd);
           gameNumber++;
        }
        return "";
    }
}
