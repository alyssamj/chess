package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMoveCalculator {
    private final ChessPiece piece;
    private final ChessBoard board;
    private ChessPosition currentPosition;

//    public PieceMoveCalculator() {}

    public PieceMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition currentPostion) {
        this.piece = piece;
        this.board = board;
        this.currentPosition = currentPostion;
    }

    public Collection<ChessMove> possibleMoves() {
        Rook myRook = new Rook(board, currentPosition);
        return myRook.rookPossibleDirections();
    }


    private class Bishop {//extends PieceMoveCalculator {


        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private Collection<ChessMove> possibleMoves;

        private Bishop(ChessBoard board, ChessPosition currentPosition) {
        //    super();
            this.board = board;
            this.currentPosition = currentPosition;
        }

        private Collection<ChessMove> bishopPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

//            for (int i = row+1 && int j = col+1; i<=8 && j <= 8; i++ && j++) {
//
//            }
            return myMoves;
        }
    }
    private class Queen{
        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private Collection<ChessMove> possibleMoves;

        private Queen(ChessBoard board, ChessPosition currentPosition) {
            //super();
            this.board = board;
            this.currentPosition = currentPosition;
        }

        private Collection<ChessMove> queenPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            for (int i = col+1; i <= 8; ++i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = col-1; i >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row+1; i <= 8; ++i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row-1; i >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }


            return myMoves;

        }

}



    private class Rook{ //extends PieceMoveCalculator {

        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private Collection<ChessMove> possibleMoves;

        private Rook(ChessBoard board, ChessPosition currentPosition) {
            //super();
            this.board = board;
            this.currentPosition = currentPosition;
        }

        private Collection<ChessMove> rookPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            //
            for (int i = col+1; i <= 8; ++i) {
               ChessPosition mySpot = new ChessPosition(row, i);
               ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
               myMoves.add(myMove);
            }
            for (int i = col-1; i >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row+1; i <= 8; ++i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row-1; i >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            return myMoves;
        }
    }
}
/** Things that need to happen
 * I need to get each piece that's on the board and show it's position
 * Check to see in each position if there's a piece there
 * If no piece, add position to collection
 * once there is a piece/hits edge, stop loop
 *
 * while (board.getPiece
 */