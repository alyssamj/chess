package ui;

import requestsandresponses.*;
import serverfacade.ServerFacade;
import websocket.MessageHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChessClient {

    private final String serverUrl;
    private final int port;
    private final ServerFacade server;
    private String authToken;
    public Map<Integer, Integer> gameMap = new HashMap<>();

    public ChessClient(String serverUrl, int port) {
        this.port = port;
        server = new ServerFacade(port);
        this.serverUrl = serverUrl;
    }

    public Object evalPreLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> helpLogin();
                case "quit" -> quit();
                case "login" -> login(params);
                case "register" -> register(params);
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public Object evalPostLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "quit"-> quit();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                default -> helpPost();
            };
        } catch (Exception e) {
            throw new RuntimeException("Unable to get command " + e.getMessage());
        }
    }

    public Object evalGamePlay(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "quit":
                    logout();
                    return quit();
                default:
                    return quit();
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to register command" + e.getMessage());
        }
    }

    private Object quit() {
        return "quit";
    }

    private Object helpPost() {
        return """
                create <GAMENAME> - create a new game
                list - list games
                join <ID> [WHITE|BLACK] - join a game as white or black
                observe <ID> - watch a game
                logout - logout when finished
                quit - stop playing chess
                help - possible commands
                """;
    }

    public Object helpLogin() {

        return """
        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
        login <USERNAME> <PASSWORD> - to play chess
        quit - stop playing chess
        help - possible commands
        """;
    }

    public Object logout() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResult logoutResult = server.logout(logoutRequest);
        if (logoutResult == null) {
            return "error logging out";
        }
        if (logoutResult.message() != null) {
            return logoutResult.message();
        }
        return "logged out - press help to see commands";
    }

    public Object register(String[] params) {
        if (params.length < 3) {
            return "Error: need username, password and email";
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = server.register(registerRequest);
        if (registerResult == null) {
            return "please try again";
        }
        authToken = registerResult.authToken();
        if (authToken != null) {
            String[] loginParams = {params[0], params[1]};
            this.login(loginParams);
        }
        return "registered - press help";
    }

    public Object login(String[] params) {
        if (params.length != 2) {
            return "please only give username and password";
        }
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = server.login(loginRequest);
        if (loginResult == null) {
            return "unable to login. Please try again";
        }
        authToken = loginResult.authToken();
        return "logged in - press help";

    }

    public Object createGame(String[] params) {
        String gameName = params[0];
        CreateRequest createRequest = new CreateRequest(authToken, gameName);
        CreateResult createResult = server.createGame(createRequest);
        if (createResult == null) {
            return "unable to create game";
        }
        return "gameID of " + gameName + ": "  + String.valueOf(createResult.gameID());
    }

    public Object listGames(String[] params) {
        ListRequest listRequest = new ListRequest(authToken);
        ListResult listResult = server.listGames(listRequest);
        StringBuilder stringBuilder = new StringBuilder();
        int gameNumber = 1;
        //for (ArrayListResult game : listResult.games()) {
        for (int i = 0; i < listResult.games().length; i++) {
            ArrayListResult game = listResult.games()[i];
            int gameID = game.gameID();
//            int gameID = game.gameID();
            if (!gameMap.containsKey(gameNumber)) {
                gameMap.put(gameNumber, gameID);
            }
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            String gameName = game.gameName();
            String gameFull;
            if (blackUsername != null && whiteUsername != null) {
                gameFull = "Full Game";
            } else if (blackUsername != null && whiteUsername == null) {
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
                                      
                    """, gameID, gameName, gameFull, whiteUsername, blackUsername);
            stringBuilder.append(stringToAdd);
            gameNumber++;
        }
        return stringBuilder.toString();
    }

    public Object joinGame(String[] params) {
        if (!isInteger(params[0])) {
            return "invalid gameID";
        }
        int gameID = Integer.parseInt(params[0]);
        String playerColor = params[1];
        System.out.println(playerColor);
        if (!playerColor.contains("white") && !playerColor.contains("black")) {
            return "invalid player color";
        }
        JoinRequest joinRequest = new JoinRequest(authToken, playerColor, gameID);
        JoinResult joinResult = server.joinGame(joinRequest);
        if (joinResult == null) {
            return "unable to join game";
        }
        if (joinResult.message() != null) {
            return joinResult.message();
        }
        GameplayREPL gameplayREPL = new GameplayREPL(this, playerColor, serverUrl, gameID, authToken);
        return gameplayREPL;
    }

    public Object observeGame(String[] params) {
        int gameToJoin = Integer.parseInt(params[0]);
        return "now observing game " + String.valueOf(gameToJoin);
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("-?\\d+");
    }
}