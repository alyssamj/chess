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
                        GameplayREPL gameplayREPL = (GameplayREPL) result;
                        gameplayREPL.setState(GameplayREPL.StateOfPlayer.PLAYER);
                        gameplayREPL.run();
                    }
                    else {
                        System.out.println((String) result);
                    }
                } else if (line.contains("observe")) {
                    GameplayREPL gameplayREPL = (GameplayREPL) result;
                    gameplayREPL.setState(GameplayREPL.StateOfPlayer.OBSERVER);
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

