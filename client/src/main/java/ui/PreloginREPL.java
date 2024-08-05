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
        while (!result.equals("quit")) {
            loggedIn = false;
            printPrompt();
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

    private void printPrompt() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess\n");
        System.out.println("quit - stop playing chess\n");
        System.out.println("help - possible commands\n");
    }


}
