package ui;

import chess.ChessGame;
import chess.ChessPosition;
import websocket.MessageHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class GameClient {
    private WebSocketFacade webSocketFacade;
    private MessageHandler messageHandler;
    private ChessClient client;
    private ChessGame chessGame;

    public GameClient(WebSocketFacade webSocketFacade, MessageHandler messageHandler, ChessClient client, ChessGame chessGame) {
        this.webSocketFacade = webSocketFacade;
        this.messageHandler = messageHandler;
        this.client = client;
        this.chessGame = chessGame;
    }


    public String evalGamePlay(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "move" -> makeMove(params);
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public String makeMove(String[] params) {
        if (params.length < 2) {
            return "Error: Invalid move command. Please try again";
        }
        String startPosition = params[0];
        String endPosition = params[1];

        int row = startPosition.charAt(1);
        Character column = startPosition.charAt(0);
        int col = 0;
        switch (column) {
            case 'a' : row = 1;
            case 'b' : row = 2;
            case 'c' : row = 3;
            case 'd' : row = 4;
            case 'e' : row = 5;
            case 'f' : row = 6;
            case 'g' : row = 7;
            case 'h' : row = 8;
        }

        return "";
    }

    public String help() {
        return """
                redraw - redraw chess board
                leave - leave game
                move <START> <END> - make a move
                resign - forfeit the game
                highlight - shows legal moves
                help - possible commands
                """;
    }

}
