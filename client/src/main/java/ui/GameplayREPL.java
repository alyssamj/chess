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
        if (playerColor.equals("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else if (playerColor.equals("white")){
            teamColor = ChessGame.TeamColor.WHITE;
        } else {
            teamColor = null;
        }

    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ChessConsole chessConsole = new ChessConsole();
        var result = "";
        System.out.println(printPrompt());
        while (!result.contains("quit") && !result.equals("resign")) {
            String line = scanner.nextLine();
            result = client.evalPostLogin(line);
            try {
                if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
                    chessConsole.whiteBoard();
                } else if (teamColor.equals(ChessGame.TeamColor.BLACK)) {
                    chessConsole.blackBoard();

                } else {
                    chessConsole.whiteBoard();
                }

            } catch (Throwable e) {
                throw e;
            }

        }
    }

    private static String printPrompt() {
        return """
                You are now playing
                """;
    }
}

