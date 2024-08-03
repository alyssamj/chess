package ui;

import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient {
    private final String serverUrl;
    private final ServerFacade server;
    private String authToken;

    public PostLoginClient(String serverUrl, ServerFacade server) {
        this.server = server;
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private static String help() {
        return "";
    }

}
