package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessConsole {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
//    private String[][] myBoard;
    private static ChessBoard myBoard;

    private static Random rand = new Random();

//    public ChessConsole() {
//        myBoard = new ChessBoard();
//    }


    public static void main(String[] args) { //drawChessBoard() {
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessGame(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(" ");
        for (int boardCol = 0; boardCol < headers.length; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(SET_BG_COLOR_BLACK);

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(EMPTY);
        out.print(headerText);
        out.print(" ");
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessGame(PrintStream out) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
//                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, int row) {
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

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
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

    private static boolean isEven(int number) {
        return number % 2 == 0;
    }
}

//        for (int squareRow = 0; squareRow < 8; squareRow++) {
//            out.print(" "+(squareRow + 1));
//            for (int boardCol = 0; boardCol < 8; boardCol++) {
//                setWhite(out);
//                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
//                    setColor(out, squareRow, boardCol);
////                    if (boardCol % 2 == 0) {
////                        setBlack(out);
////                        out.print(EMPTY.repeat(prefixLength));
////                        printDarkSquare(out, rand.nextBoolean() ? X : O);
////                        out.print(EMPTY.repeat(suffixLength));
////                        setBlack(out);
////                    } else {
////                        out.print(EMPTY.repeat(prefixLength));
////                        printPlayer(out, rand.nextBoolean() ? X : O);
////                        out.print(EMPTY.repeat(suffixLength));
////                    }
//                }
//                setBlack(out);
//            }

//    private static void drawHorizontalLine(PrintStream out) {
//
//        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
//                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;
//
//        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
//            setBlue(out);
//            out.print(EMPTY.repeat(boardSizeInSpaces));
//
//            setBlack(out);
//            out.println();
//        }