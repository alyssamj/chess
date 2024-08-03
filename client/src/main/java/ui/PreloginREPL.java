package ui;

import java.util.Scanner;

public class PreloginREPL {
    private final ChessClient client;

    public PreloginREPL(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.evalPreLogin(line);
                System.out.println(result);
                if (line.contains("register") || line.contains("login")) {
                    PostloginREPL postloginREPL = new PostloginREPL(client);
                    postloginREPL.run();
                }
            } catch (Throwable e) {
                throw e;
            }

        }
    }

    private void printPrompt() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess\n");
        System.out.println("quit - stop playing chess\n");
        System.out.println("help - possible commands\n");
    }


}
