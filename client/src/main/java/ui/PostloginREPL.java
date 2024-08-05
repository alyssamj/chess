package ui;

import requestsandresponses.JoinResult;

import java.util.Scanner;

public class PostloginREPL {
    private final ChessClient client;

    public PostloginREPL(ChessClient client) {
        this.client = client;
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !result.equals("logged out")) {
            String line = scanner.nextLine();

            try {
                result = client.evalPostLogin(line);
                System.out.println(result);
                if (result.contains("joined game") || result.contains("now observing game")) {
                    String playerColor = null;
                    if (result.contains("black")) {
                        playerColor = "black";
                    } else if (result.contains("white")) {
                        playerColor = "white";
                    }
                    GameplayREPL gameplayREPL = new GameplayREPL(client, playerColor);
                    gameplayREPL.run();
                }
            } catch (Throwable e) {
                throw e;
            }
        }
    }

}
