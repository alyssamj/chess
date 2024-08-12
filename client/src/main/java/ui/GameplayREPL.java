package ui;

import chess.*;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.MessageHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.Session;
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
    public StateOfPlayer state;


    public enum StateOfPlayer {
        PLAYER,
        OBSERVER
    }

    public StateOfPlayer getState() {
        return state;
    }
    public void setState(StateOfPlayer state) {
        this.state = state;
    }

    public GameplayREPL(ChessClient client, String playerColor, String serverUrl, int gameID, String authToken) {
        this.client = client;
        this.serverUrl = serverUrl;
        this.gameID = gameID;
        this.authToken = authToken;
        webSocketFacade = new WebSocketFacade(serverUrl, this);
     //   this.state = StateOfPlayer.PLAYER;
        if (playerColor == null) {
            teamColor = null;
        } else {
            playerColor.toLowerCase();
            if (playerColor.equals("black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                teamColor = ChessGame.TeamColor.WHITE;
            }
        }
    }

    public WebSocketFacade getWebSocketFacade() {
        return webSocketFacade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        webSocketFacade.connectToGame(gameID, authToken);
        while (chessGame == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        chessBoard = chessGame.getBoard();
        chessConsole = new ChessConsole(chessBoard);
        var result = "";
        System.out.println(printPrompt());
        if (teamColor == null) {
            chessConsole.blackBoard();
        }
        else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            chessConsole.whiteBoard();
        } else {
            chessConsole.blackBoard();
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        while (!result.contains("leave")) {
            String line = scanner.nextLine();
            if (state == StateOfPlayer.OBSERVER) {
                result = observeEval(line);
            } else {
                result = evalGamePlay(line);
            }
//            try {
//                if (teamColor == null) {
//                    chessConsole.whiteBoard();
//                    chessConsole.blackBoard();
//                }
//                else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
//                    chessConsole.whiteBoard();
//                } else {
//                    chessConsole.blackBoard();
//                }
//            } catch (Throwable e) {
//                throw e;
//            }
            chessConsole.updateChessBoard(chessBoard);
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
        System.out.println("You have left the game");
    }

    public void runObserve() {
        Scanner scanner = new Scanner(System.in);
        webSocketFacade.connectToGame(gameID, authToken);
        while (chessGame == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        chessBoard = chessGame.getBoard();
        ChessConsole observeBoard = new ChessConsole(chessBoard);
        observeBoard.whiteBoard();
        var result = "";
        while (!result.contains("leave")) {
            observeBoardHelp();
            String line = scanner.nextLine();
            result = evalGamePlay(line);
            if (line.contains("highlight")) {

            } else if (line.contains("redraw")) {

            }
            observeBoard.updateChessBoard(chessGame.myBoard);
        }
    }

    private String observeEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "connect" -> connect(params);
                case "highlight" -> observeHighlight(params[0]);
                case "redraw" -> observeRedraw();
                case "leave" -> leave();
                default -> observeBoardHelp();
            };
        } catch (Exception e) {
            System.out.println("Something went wrong, please try again");
        }
        return "";
    }



    private String observeBoardHelp() {
        System.out.println("""
                redraw - redraws board
                highlight <POSITION> - highlights available moves for piece at position
                """);
        return "";
    }

    private String observeRedraw() {
        chessConsole.whiteBoard();
        return  "";
    }

    private String observeHighlight(String input) {
        return "";
    }


    private String redrawChessBoard(ChessGame.TeamColor playerColor) {
        if (playerColor == null) {
            chessConsole.whiteBoard();
        } else if (playerColor == ChessGame.TeamColor.WHITE) {
            chessConsole.whiteBoard();
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            chessConsole.blackBoard();
        }
        return "";
    }

    public static String printPrompt() {
        return """
                Enter command to see board;
                """;
    }

    @Override
    public void notify(Notification notification) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + notification.getMessage());
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);

    }

    @Override
    public void error(ErrorMessage errorMessage) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getMessage());
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    @Override
    public void loadGame(LoadGameMessage loadGameMessage) {
        this.chessGame = loadGameMessage.game;
    }

    public String evalGamePlay(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "connect" -> connect(params);
                case "move" -> makeMove(params);
                case "redraw" -> redrawChessBoard(teamColor);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
        return "";
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()) {
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                chessGame = loadGameMessage.getGame();
                break;
            case NOTIFICATION:
                Notification notification = new Gson().fromJson(message, Notification.class);
                String myMessage = notification.getMessage();
                notify(notification);
                break;
            case ERROR:
                ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                String errorMessage = error.getMessage();
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage);
                break;
        }
    }

    public String connect(String[] params) {
        try {
            webSocketFacade.connectToGame(gameID, authToken);
        } catch (Exception ex) {
            String message = "Unable to connect to the game. Please try again";
            System.out.println(message);
        }
        chessGame = webSocketFacade.getChessGame();
        return "";
    }

    public String makeMove(String[] params) {
        if (chessGame.getState() == ChessGame.GameState.GAME_OVER) {
            System.out.println("Game is already over");
            return "";
        }
        if (params.length < 2) {
            System.out.println("Error: Invalid move command. Please try again");
            return "";
        }
        String startPosition = params[0];
        String endPosition = params[1];
        String upgradePiece;
        if (params.length > 2) {
            upgradePiece = params[2];
        }
        upgradePiece = null;

        ChessPosition startPos = getPosition(startPosition);
        ChessPosition endPos = getPosition(endPosition);

        if (chessBoard.getPiece(startPos) != null) {
            ChessMove chessMove = new ChessMove(startPos, endPos, checkForPawn(startPos, endPos, upgradePiece));
            webSocketFacade.makeMove(authToken, gameID, chessMove, teamColor);
            try {
                chessGame.makeMove(chessMove);
            } catch (InvalidMoveException e) {
                System.out.println("invalid move. Try again");
            }
        } else {
            System.out.println("no piece found at location");
        }
        return "";
    }

    private ChessPosition getPosition(String startPosition) {
        int startRow =  Character.getNumericValue(startPosition.charAt(1));
        Character startColumn = startPosition.charAt(0);
        int startCol = getColumn(startColumn);
        ChessPosition startPos = new ChessPosition(startRow, startCol);
        return startPos;
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

    private String resign() {
        try {
            webSocketFacade.resign(authToken, gameID, teamColor);
        } catch (Exception e) {
            System.out.println("Something went wrong. Please try again");
        }
        return "";
    }

    private String leave() {
        try {
            webSocketFacade.leaveGame(authToken, gameID, teamColor);
        } catch (Exception e) {
            System.out.println("unable to leave game");
        }
        return "leave";
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

    public String help() {
        System.out.println("""
                redraw - redraw chess board
                leave - leave game
                move <START> <END> - make a move
                resign - forfeit the game
                highlight - shows legal moves
                help - possible commands
                """);
        return "";
    }
}


