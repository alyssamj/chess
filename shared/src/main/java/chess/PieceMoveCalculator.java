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

//    private class Bishop extends PieceMoveCalculator {
//
//
//        private final ChessBoard board;
//        private final ChessPosition currentPosition;
//        private Collection<ChessMove> possibleMoves;
//
//        private Bishop(ChessBoard board, ChessPosition currentPosition) {
//            super();
//            this.board = board;
//            this.currentPosition = currentPosition;
//        }
//
////        private Bishop possibleDirections() {
////
////        }
//    }

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
            for (int i = col; i < 8; ++i) {
               ChessPosition mySpot = new ChessPosition(row, i);
               ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
               myMoves.add(myMove);
            }
            for (int i = col; i > 1; --i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row; i < 8; ++i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (int i = row; i > 1; --i) {
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