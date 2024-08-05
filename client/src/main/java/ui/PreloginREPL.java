package ui;

import model.UserData;

import java.util.Scanner;

public class PreloginREPL {
    private final ChessClient client;
    private boolean loggedIn = false;
    private UserData user = null;

    public PreloginREPL(String serverUrl, int port) {
        client = new ChessClient(serverUrl, port);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        System.out.print("Welcome to chess: press help\n");
        while (!result.equals("quit")) {
            loggedIn = false;
            String line = scanner.nextLine();

            try {
                result = client.evalPreLogin(line);
                System.out.println(result);
                if (result.contains("logged in") || result.contains("registered")) {
                    loggedIn = true;
                    PostloginREPL postloginREPL = new PostloginREPL(client);
                    postloginREPL.run();
                }
            } catch (Throwable e) {
                throw e;
            }

        }
        loggedIn = false;
    }
}
