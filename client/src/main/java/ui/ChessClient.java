package ui;

import requestsandresponses.LoginRequest;
import requestsandresponses.LoginResult;
import requestsandresponses.RegisterRequest;
import requestsandresponses.RegisterResult;
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

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                default -> help();
//                case "quit" -> quit();
//                case "login";
                case "register" -> register(params);
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public String help() {

        return "register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n  login <USERNAME> <PASSWORD> - to play chess\n  quit - stop playing chess\nhelp - possible commands\n";
    }

    public String quit() {

        return "";
    }

    public String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = server.register(registerRequest);
        authToken = registerResult.authToken();
        return "";
    }

    public String login(String[] params) {
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = server.login(loginRequest);
    }
}
