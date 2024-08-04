package ui;

import java.util.Scanner;

public class GameplayREPL {
    private final ChessClient client;

    public GameplayREPL(ChessClient client) {
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ChessConsole chessConsole = new ChessConsole();
        var result = "";
        System.out.println(printPrompt());
        while (!result.equals("quit") || !result.equals("resign")) {
            String line = scanner.nextLine();
            chessConsole.blackBoard();
            try {
                result = client.evalPostLogin(line);
                System.out.println(result);
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

