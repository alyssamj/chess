package ui;

import chess.ChessGame;

import java.util.Scanner;

public class GameplayREPL {
    private final ChessClient client;
   // private final String username;
    private final ChessGame.TeamColor teamColor;

    public GameplayREPL(ChessClient client, String playerColor) {
        this.client = client;
        //this.username = username;
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
        var result = "";
        System.out.println(printPrompt());
        while (!result.contains("quit") && !result.equals("resign")) {
            String line = scanner.nextLine();
            result = client.evalGamePlay(line);
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

    private static String printPrompt() {
        return """
                Press help to see chess game
                """;
    }
}

