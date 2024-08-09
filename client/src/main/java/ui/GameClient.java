package ui;

import chess.*;
import websocket.MessageHandler;
import websocket.WebSocketFacade;
import websocket.commands.MakeMove;

import java.util.Arrays;

public class GameClient {
    private WebSocketFacade webSocketFacade;
    private GameplayREPL gameplayREPL;
    private ChessClient client;
    private ChessBoard chessBoard;
    private ChessGame chessGame;

    public GameClient(WebSocketFacade webSocketFacade, GameplayREPL messageHandler, ChessClient client) {
        this.webSocketFacade = webSocketFacade;
        this.gameplayREPL = messageHandler;
        this.client = client;
    }


    public Object evalGamePlay(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "connect" -> connect(params);
           //     case "move" -> makeMove(gameplayREPL.authToken, gameplayREPL.gameID, );
                default -> help();
            };
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public ChessGame connect(String[] params) {
        webSocketFacade.connectToGame(gameplayREPL.gameID, gameplayREPL.authToken);
        if (chessGame == null) {
            return null;
        }
        chessBoard = chessGame.getBoard();
        return chessGame;
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) {
        try {
            webSocketFacade.makeMove(authToken, gameID, move);
        } catch (Exception e) {
            throw new RuntimeException("Error sending move:");
        }
    }

    private ChessPiece checkForPawn(ChessPosition startPosition, ChessPosition endPosition, String upgradePiece) {
       if (chessBoard.getPiece(startPosition) == null) {
           return null;
       } else if (chessBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
           if (chessBoard.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) {

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

    public String help() {
        return """
                redraw - redraw chess board
                leave - leave game
                move <START> <END> - make a move
                resign - forfeit the game
                highlight - shows legal moves
                help - possible commands
                """;
    }

}
