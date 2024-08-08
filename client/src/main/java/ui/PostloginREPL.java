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
                result = client.evalPostLogin(line);
                if (result instanceof String) {
                    System.out.println(result);
                } else if (result instanceof GameplayREPL) {

                }
                if (line.contains("join")) {
                    GameplayREPL gameplayREPL = client.evalPostLogin(line);
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
                    if (client.evalPostLogin(line).getClass() == String.class) {
                        result = client.evalPostLogin(line);
                        System.out.println(result);
                    }
                }
            } catch(Throwable e){
                throw e;
            }
        }
    }
}

