package ui;

import chess.ChessGame;
import websocket.MessageHandler;
import websocket.WebSocketFacade;
import websocket.messages.Notification;

import java.util.Scanner;

public class GameplayREPL implements MessageHandler{
    private ChessClient client;
    public ChessGame.TeamColor teamColor;
    private WebSocketFacade webSocketFacade;
    private MessageHandler messageHandler;
    public String serverUrl;
    public int gameID;
    public String authToken;

    public GameplayREPL(ChessClient client, String playerColor, String serverUrl, int gameID, String authToken) {
        this.client = client;
        this.serverUrl = serverUrl;
        this.gameID = gameID;
        this.authToken = authToken;
        messageHandler = new MessageHandler() {
            @Override
            public void notify(Notification notification) {
                printPrompt();
            }
        };
        webSocketFacade = new WebSocketFacade(serverUrl, messageHandler);
        if (playerColor == null) {
            teamColor = null;
        }
        else if (playerColor.equals("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else{
            teamColor = ChessGame.TeamColor.WHITE;
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ChessConsole chessConsole = new ChessConsole();
        GameClient gameClient = new GameClient(webSocketFacade, messageHandler, client, this);
        var result = "";
        System.out.println(printPrompt());
        if (teamColor == null) {
            chessConsole.whiteBoard();
            chessConsole.blackBoard();
        }
        else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            chessConsole.whiteBoard();
        } else {
            chessConsole.blackBoard();
        }
        while (!result.contains("quit") && !result.equals("resign")) {
            String line = scanner.nextLine();
            result = client.evalGamePlay(line).toString();
            try {
                if (teamColor == null) {
                    chessConsole.whiteBoard();
                    chessConsole.blackBoard();
                }
                else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
                    chessConsole.whiteBoard();
                } else {
                    chessConsole.blackBoard();
                }
            } catch (Throwable e) {
                throw e;
            }

        }
    }

    public static String printPrompt() {
        return """
                Enter command to see board;
                """;
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification.getMessage());
    }
}

