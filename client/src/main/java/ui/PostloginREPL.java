package ui;

import java.util.Scanner;

public class PostloginREPL {
    private final ChessClient client;

    public PostloginREPL(ChessClient client) {
        this.client = client;
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = client.evalPostLogin(line);
                System.out.println(result);
            } catch (Throwable e) {
                throw e;
            }

        }
    }

}
