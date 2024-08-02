package ui;

import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {

    private final String serverUrl;
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> help();
//                case "quit";
//                case "login";
//                case "register"
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public String help() {
        return """
                
                """;
    }
}
