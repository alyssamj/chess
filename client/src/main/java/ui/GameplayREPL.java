package ui;

import java.util.Scanner;

public class GameplayREPL {
    private final ChessClient client;

    public GameplayREPL(ChessClient client) {
        this.client = client;
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        ChessConsole chessConsole = new ChessConsole();
        var result = "";
        System.out.println(printPrompt());
        while (!result.equals("quit") || !result.equals("resign")) {
            String line = scanner.nextLine();
           // chessConsole.drawChessBoard();
            try {
//              //  result = client.evalPostLogin(line);
//                System.out.println(result);
//                if (line == "join game") {
//                    GameplayREPL gameplayREPL = new GameplayREPL(client);
//                }
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

