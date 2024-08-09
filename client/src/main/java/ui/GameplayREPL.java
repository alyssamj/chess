package ui;

import chess.*;
import websocket.MessageHandler;
import websocket.WebSocketFacade;
import websocket.messages.Notification;

import java.util.Arrays;
import java.util.Scanner;

public class GameplayREPL implements MessageHandler{
    private ChessClient client;
    public ChessGame.TeamColor teamColor;
    public String serverUrl;
    public int gameID;
    public String authToken;
    private ChessGame chessGame;
    private ChessBoard chessBoard;
    private ChessConsole chessConsole;
    public WebSocketFacade webSocketFacade;

    public GameplayREPL(ChessClient client, String playerColor, String serverUrl, int gameID, String authToken) {
        this.client = client;
        this.serverUrl = serverUrl;
        this.gameID = gameID;
        this.authToken = authToken;
        webSocketFacade = new WebSocketFacade(serverUrl, this);
        if (playerColor == null) {
            teamColor = null;
        }
        else if (playerColor.equals("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else{
            teamColor = ChessGame.TeamColor.WHITE;
        }
    }

    public WebSocketFacade getWebSocketFacade() {
        return webSocketFacade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        chessConsole = new ChessConsole(chessBoard);
        var result = "";
        System.out.println(printPrompt());
        if (teamColor == null) {
            chessConsole.whiteBoard();
            chessConsole.blackBoard();
        }
        else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            chessConsole.whiteBoard();
        } else {
            chessConsole.blackBoard();
        }
        while (!result.contains("quit") && !result.equals("resign")) {
            String line = scanner.nextLine();
            result = client.evalGamePlay(line).toString();
            try {
                if (teamColor == null) {
                    chessConsole.whiteBoard();
                    chessConsole.blackBoard();
                }
                else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
                    chessConsole.whiteBoard();
                } else {
                    chessConsole.blackBoard();
                }
            } catch (Throwable e) {
                throw e;
            }

        }
    }

    private void redrawChessBoard(ChessGame.TeamColor playerColor) {
        if (playerColor == null) {
            chessConsole.blackBoard();
            chessConsole.whiteBoard();
        } else if (playerColor == ChessGame.TeamColor.WHITE) {
            chessConsole.whiteBoard();
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            chessConsole.blackBoard();
        }
    }

    public static String printPrompt() {
        return """
                Enter command to see board;
                """;
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification.getMessage());
    }

    public void evalGamePlay(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "connect" -> connect(params);
                case "move" -> makeMove(params);
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void connect(String[] params) {
        try {
            webSocketFacade.connectToGame(gameID, authToken);
        } catch (Exception ex) {
            String message = "Unable to connect to the game. Please try again";
        }
        chessGame = webSocketFacade.getChessGame();
    }

    public void makeMove(String[] params) {
        if (params.length < 2) {
            System.out.println("Error: Invalid move command. Please try again");
        }
        String startPosition = params[0];
        String endPosition = params[1];
        String upgradePiece = params[2];

        int startRow = startPosition.charAt(1);
        Character startColumn = startPosition.charAt(0);
        int startCol = getColumn(startColumn);
        ChessPosition startPos = new ChessPosition(startRow, startCol);

        int endRow = endPosition.charAt(1);
        Character endColumn = endPosition.charAt(0);
        int endCol = getColumn(endColumn);
        ChessPosition endPos = new ChessPosition(endRow, endCol);
        if (chessBoard.getPiece(startPos) != null) {
            ChessMove chessMove = new ChessMove(startPos, endPos, checkForPawn(startPos, endPos, upgradePiece));
            webSocketFacade.makeMove(authToken, gameID, chessMove);
        } else {
            System.out.println("no piece found at location");
        }
        chessGame = webSocketFacade.getChessGame();
    }



    private ChessPiece.PieceType checkForPawn(ChessPosition startPosition, ChessPosition endPosition, String upgradePiece) {
        if (chessBoard.getPiece(startPosition) == null) {
            return null;
        } else if (chessBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (chessBoard.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {
                return checkForPawn(startPosition, endPosition, upgradePiece);
            }
            else if (chessBoard.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1) {
                return checkForPawn(startPosition, endPosition, upgradePiece);
            }
        }
        return null;
    }

    private ChessPiece.PieceType upgradePiece(String upgradePiece, String playerColor) {
        String upgrade = upgradePiece.toLowerCase();
        return switch (upgrade) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
    }



    private int getColumn(Character column) {
        int col = 0;
        switch (column) {
            case 'a' : col = 1;
            case 'b' : col = 2;
            case 'c' : col = 3;
            case 'd' : col = 4;
            case 'e' : col = 5;
            case 'f' : col = 6;
            case 'g' : col = 7;
            case 'h' : col = 8;
        }
        return col;
    }

    public void help() {
        System.out.println("""
                redraw - redraw chess board
                leave - leave game
                move <START> <END> - make a move
                resign - forfeit the game
                highlight - shows legal moves
                help - possible commands
                """);
    }

}


