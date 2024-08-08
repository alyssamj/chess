package ui;

import java.util.Scanner;

public class PostloginREPL {
    private final ChessClient client;
    private String serverUrl;

    public PostloginREPL(ChessClient client, String serverUrl) {
        this.client = client;
        this.serverUrl = serverUrl;

    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !result.equals("logged out")) {
            String line = scanner.nextLine();

            try {
                if (line.contains("join")) {
                    GameplayREPL gameplayREPL = (GameplayREPL) client.evalPostLogin(line);
                   // if (result.contains("joined game") || result.contains("now observing game")) {
                        String playerColor = null;
                        if (result.contains("black")) {
                            playerColor = "black";
                        } else if (result.contains("white")) {
                            playerColor = "white";
                        }
                        gameplayREPL.run();
                  //  }
                } else {
                    result = client.evalPostLogin(line).toString();
                }
            } catch(Throwable e){
                throw e;
            }
        }
    }
}

