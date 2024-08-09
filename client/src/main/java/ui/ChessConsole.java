package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;


public class ChessConsole {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private ChessBoard myBoard;

    public ChessConsole(ChessBoard chessBoard) {
        this.myBoard = chessBoard;
    }

    public void highlightWhiteMoves() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, ChessGame.TeamColor.WHITE);

    }


    public void whiteBoard() {
     var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
     out.print(ERASE_SCREEN);
     ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
     drawHeaders(out, teamColor);
     drawChessBoardWhite(out);
     drawHeaders(out, teamColor);
     out.print(SET_BG_COLOR_BLACK);
     out.print(SET_TEXT_COLOR_WHITE);
    }

    public void blackBoard() { //drawChessBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
        drawHeaders(out, teamColor);

        drawChessGameBlack(out);

        drawHeaders(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor teamColor) {
        String[] headers = getHeaders(teamColor);
        setBlack(out);
        out.print(" ");
        for (int boardCol = 0; boardCol < headers.length; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(SET_BG_COLOR_BLACK);

        out.println();
    }

    private static String[] getHeaders(ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h"};
            return headers;
        } else {
            String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};
            return headers;
        }
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(EMPTY);
        out.print(headerText);
        out.print(" ");
    }

    private  void drawChessGameBlack(PrintStream out) {
        for (int row = 8; row >= 1; row--) {
            drawRowOfSquares(out, row);
            if (row < BOARD_SIZE_IN_SQUARES -1) {
                setBlack(out);
            }
        }
    }

    private void drawChessBoardWhite(PrintStream out) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }
    }

    private void highLightChessBoardWhite(PrintStream out, ArrayList<ChessPosition> legalMoves) {


    }


    private  void drawRowOfSquares(PrintStream out, int row) {
        String[] side = {"8", "7", "6", "5", "4", "3", "2", "1"};
        setBlack(out);
        out.print(side[row-1] + " ");
        for (int col = 1; col < 9; col++) {
            ChessPiece piece = myBoard.getPiece(new ChessPosition(row, col));
            if ((row + col) % 2 == 0) {
                printDarkSquare(out, piece);
            } else {
                printLightSquare(out, piece);
            }
        }
        setBlack(out);
        out.print(" "+side[row-1]);
        out.println();
    }



    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printLightSquare(PrintStream out, ChessPiece piece) {
        out.print(SET_BG_COLOR_WHITE);
        String player = getPlayerForPiece(out, piece);
        if (piece == null) {
            out.print("  " +EMPTY);
        } else {
            out.print(" " + player + " ");
        }
        setWhite(out);
    }

    private static void printLightHighlightSquare(PrintStream out, ChessPiece piece) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(" "+EMPTY);
        setWhite(out);
    }


    private static String getPlayerForPiece(PrintStream out, ChessPiece piece) {
        String stringToReturn = "";
        if (piece == null) {
            stringToReturn = EMPTY;
            return stringToReturn;
        }
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        if (teamColor == null) {
            stringToReturn = EMPTY;
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_BLACK);
            switch (piece.getPieceType()) {
                case QUEEN -> stringToReturn = BLACK_QUEEN;
                case ROOK -> stringToReturn = BLACK_ROOK;
                case KING -> stringToReturn = BLACK_KING;
                case BISHOP -> stringToReturn = BLACK_BISHOP;
                case KNIGHT -> stringToReturn = BLACK_KNIGHT;
                case PAWN -> stringToReturn = BLACK_PAWN;
            }
        } else {
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
            switch (piece.getPieceType()) {
                case QUEEN -> stringToReturn = WHITE_QUEEN;
                case ROOK -> stringToReturn = WHITE_ROOK;
                case KING -> stringToReturn = WHITE_KING;
                case BISHOP -> stringToReturn = WHITE_BISHOP;
                case KNIGHT -> stringToReturn = WHITE_KNIGHT;
                case PAWN -> stringToReturn = WHITE_PAWN;
            }
        }
        return stringToReturn;
    }

    private static void printDarkSquare(PrintStream out, ChessPiece piece) {
        out.print(SET_BG_COLOR_MAGENTA);
        String player = getPlayerForPiece(out, piece);
        if (piece == null) {
            out.print("  " +EMPTY);
        } else {
            out.print(" " + player + " ");
        }
        setBlack(out);
    }
}
