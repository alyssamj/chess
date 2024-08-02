package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessConsole {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = " ";
    private static final String X = "X";
    private static final String O = "O";
    private String[][] myBoard;

    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawTicTacToeBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h"};
        String[] leftSide = {"1", "2", "3", "4", "5", "6", "7", "8"};
        out.print("    ");
        for (int boardCol = 0; boardCol < headers.length; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(SET_BG_COLOR_BLACK);

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(" ");
        out.print(headerText);
        out.print(" ");
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(player);

        setBlack(out);
    }

    private static void drawTicTacToeBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
//                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < 8; squareRow++) {
            out.print(" "+(squareRow + 1));
            for (int boardCol = 0; boardCol < 8; boardCol++) {
                setWhite(out);
                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    setColor(out, squareRow, boardCol);
//                    if (boardCol % 2 == 0) {
//                        setBlack(out);
//                        out.print(EMPTY.repeat(prefixLength));
//                        printDarkSquare(out, rand.nextBoolean() ? X : O);
//                        out.print(EMPTY.repeat(suffixLength));
//                        setBlack(out);
//                    } else {
//                        out.print(EMPTY.repeat(prefixLength));
//                        printPlayer(out, rand.nextBoolean() ? X : O);
//                        out.print(EMPTY.repeat(suffixLength));
//                    }
                }
                setBlack(out);
            }
        }
        out.println();
    }

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


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printLightSquare(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

    private static void setColor(PrintStream out, int row, int col) {
        if (col == 1 || col == 3 || col == 5 || col ==7) {
            printDarkSquare(out, " O ");
        } else {
            printLightSquare(out, " X ");
        }
    }

    private static void printDarkSquare(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);
        setBlack(out);
    }

    private static boolean isEven(int number) {
        return number % 2 == 0;
    }
}

