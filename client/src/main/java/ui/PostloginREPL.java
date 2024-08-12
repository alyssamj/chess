package ui;

import websocket.MessageHandler;
import websocket.WebSocketFacade;

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
        Object result = "";
        while (!result.equals("quit") && !result.equals("logged out - press help to see commands")) {
            String line = scanner.nextLine();
            result = client.evalPostLogin(line);
            try {
                if (line.contains("join")) {
                    if (result instanceof GameplayREPL) {
                        // if (result.contains("joined game") || result.contains("now observing game")) {
                        String playerColor = null;
                        if (line.contains("black")) {
                            playerColor = "black";
                        } else if (line.contains("white")) {
                            playerColor = "white";
                        }
                        GameplayREPL gameplayREPL = (GameplayREPL) result;
                        GameClient gameClient = new GameClient(gameplayREPL.getWebSocketFacade(), gameplayREPL, client);
                        gameplayREPL.run();
                    }
                    else {
                        System.out.println((String) result);
                    }
                } else if (line.contains("observe")) {
                    GameplayREPL gameplayREPL = (GameplayREPL) result;
                    gameplayREPL.run();
                } else if(line.contains("quit")) {
                    result = "quit";
                } else {
                    result = (String) result;
                    System.out.println(result);
                }
            } catch(Throwable e){
                throw e;
            }
        }
    }
}

