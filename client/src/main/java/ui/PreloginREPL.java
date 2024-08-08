package ui;

import model.UserData;
import websocket.MessageHandler;
import websocket.messages.Notification;

import java.util.Scanner;

import static ui.GameplayREPL.printPrompt;

public class PreloginREPL {
    private final ChessClient client;
    private boolean loggedIn = false;
    private UserData user = null;
    private String serverURL;

    public PreloginREPL(String serverUrl, int port) {
        client = new ChessClient(serverUrl, port);
        this.serverURL = serverUrl;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        System.out.print("Welcome to chess: press help\n");
        while (!result.equals("quit")) {
            loggedIn = false;
            String line = scanner.nextLine();

            try {
                result = client.evalPreLogin(line).toString();
                System.out.println(result);
                if (result.contains("logged in") || result.contains("registered")) {
                    loggedIn = true;
                    PostloginREPL postloginREPL = new PostloginREPL(client, serverURL);
                    postloginREPL.run();
                }
            } catch (Throwable e) {
                throw e;
            }

        }
        loggedIn = false;
    }
}
