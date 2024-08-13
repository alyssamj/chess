package ui;

import chess.*;
import com.google.gson.Gson;
import websocket.MessageHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.util.ArrayList;
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
        String line = "";
        while (!result.contains("leave") && !line.contains("leave")) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);
            line = scanner.nextLine();
            if (state == StateOfPlayer.OBSERVER) {
                result = observeEval(line);
            } else {
                result = evalGamePlay(line);
            }
            chessConsole.updateChessBoard(chessBoard);
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        }
        System.out.println("You have left the game");
    }

    private String observeEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "connect" -> connect(params);
                case "highlight" ->{
                    highlightLegalMoves(params); yield  "";
                }
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
                leave - leave game
                """);
        return "";
    }

    private String observeRedraw() {
        chessConsole.whiteBoard();
        return  "";
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
                Enter help to see board:
                """;
    }


    @Override
    public void printMessage(String message) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message);
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    @Override
    public void loadGame(LoadGameMessage loadGameMessage) {
        this.chessGame = loadGameMessage.game;
        chessConsole.updateChessBoard(chessGame.myBoard);
        if (teamColor == null) {
            redrawChessBoard(ChessGame.TeamColor.WHITE);
        } else {
            redrawChessBoard(teamColor);
        }
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
                case "highlight" -> highlightLegalMoves(params);
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
        return "";
    }

    public void highlightLegalMoves(String[] params) {
        if (params.length != 1) {
            return;
        }
        String startPosition = params[0];
        ChessPosition position = getPosition(startPosition);
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) chessGame.validMoves(position);
        if (teamColor == ChessGame.TeamColor.WHITE) {
            chessConsole.highlightWhiteMoves(validMoves);
        } else if (teamColor == ChessGame.TeamColor.BLACK) {
            chessConsole.highlighDarkMoves(validMoves);
        } else {
            System.out.println("No moves found");
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
        String upgradePiece = null;
        if (params.length > 2) {
            upgradePiece = params[2];
            ChessPiece.PieceType chessPiece = upgradePiece(upgradePiece, teamColor);
        }

        ChessPosition startPos = getPosition(startPosition);
        ChessPosition endPos = getPosition(endPosition);

        if (chessBoard.getPiece(startPos) != null) {
            ChessMove chessMove = new ChessMove(startPos, endPos, checkForPawn(startPos, endPos, upgradePiece));
            webSocketFacade.makeMove(authToken, gameID, chessMove, teamColor);
            try {
                chessGame.makeMove(chessMove);
            } catch (InvalidMoveException e) {
                //System.out.println("invalid move. Try again");
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
        if (teamColor == ChessGame.TeamColor.BLACK) {
            startRow = getRow(startRow);
        }
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

    private ChessPiece.PieceType upgradePiece(String upgradePiece, ChessGame.TeamColor teamColor) {
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);

        return "leave";
    }

    private int getColumn(Character column) {
        int col = 0;
        switch (column) {
            case 'a' : col = 1; break;
            case 'b' : col = 2; break;
            case 'c' : col = 3; break;
            case 'd' : col = 4; break;
            case 'e' : col = 5; break;
            case 'f' : col = 6; break;
            case 'g' : col = 7; break;
            case 'h' : col = 8; break;
        }
        return col;
    }

    private int getRow(int row) {
        int newRow = 9-row;
        return newRow;
    }


    public String help() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);
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


