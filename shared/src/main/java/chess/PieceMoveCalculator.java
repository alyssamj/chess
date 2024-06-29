package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
//        Rook myRook = new Rook(board, currentPosition);
//        return myRook.rookPossibleDirections();
//        Bishop myBishop = new Bishop(board, currentPosition);
//        return myBishop.bishopPossibleDirections();
//        Queen myQueen = new Queen(board, currentPosition);
//       return myQueen.queenPossibleDirections();
//       King myKing = new King(board, currentPosition);
//       return myKing.kingPossibleDirections();
        Pawn myPawn = new Pawn(board, currentPosition, piece);
        return myPawn.findPawnMoves();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMoveCalculator that = (PieceMoveCalculator) o;
        return Objects.equals(piece, that.piece) && Objects.equals(board, that.board) && Objects.equals(currentPosition, that.currentPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, board, currentPosition);
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
            int i = 0;
            int j = 0;
            for (i = row+1, j = col+1; i <=8 && j <= 8; i++, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row-1, j = col-1; i >=1 && j >= 1; i--, j--) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row+1, j = col-1; i <=8 && j >= 1; i++, j--) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row-1, j = col+1; i >=1 && j <= 8; i--, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
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
            int i = 0;
            int j = 0;
            for (i = row+1, j = col+1; i <=8 && j <= 8; i++, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row-1, j = col-1; i >=1 && j >= 1; i--, j--) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row+1, j = col-1; i <=8 && j >= 1; i++, j--) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            for (i = row-1, j = col+1; i >=1 && j <= 8; i--, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                myMoves.add(myMove);
            }
            return myMoves;
        }

}
//    private class Rook{ //extends PieceMoveCalculator {
//
//        private final ChessBoard board;
//        private final ChessPosition currentPosition;
//        private Collection<ChessMove> possibleMoves;
//
//        private Rook(ChessBoard board, ChessPosition currentPosition) {
//            //super();
//            this.board = board;
//            this.currentPosition = currentPosition;
//        }
//
//        private Collection<ChessMove> rookPossibleDirections() {
//            int row = currentPosition.getRow();
//            int col = currentPosition.getColumn();
//            Collection<ChessMove> myMoves = new ArrayList<>();
//
//            //
//            for (int i = col+1; i <= 8; ++i) {
//                    ChessPosition mySpot = new ChessPosition(row, i);
//                    if (board.getPiece(mySpot).getTeamColor() == null) {
//                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                        myMoves.add(myMove);
//                        continue;
//                    } else if (board.getPiece(mySpot).getTeamColor() != piece.getTeamColor()) {
//                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                        myMoves.add(myMove);
//                    } else if (board.getPiece(mySpot).getTeamColor() == piece.getTeamColor()) {
//                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                        break;
//                    }
//            }
//            for (int i = col-1; i >= 1; --i) {
//                ChessPosition mySpot = new ChessPosition(row, i);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            for (int i = row+1; i <= 8; ++i) {
//                ChessPosition mySpot = new ChessPosition(i, col);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            for (int i = row-1; i >= 1; --i) {
//                ChessPosition mySpot = new ChessPosition(i, col);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            return myMoves;
//        }
//    }
    private class King { //extends PieceMoveCalculator {

        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private Collection<ChessMove> possibleMoves;

        private King(ChessBoard board, ChessPosition currentPosition) {
            //super();
            this.board = board;
            this.currentPosition = currentPosition;
        }
        private Collection<ChessMove> kingPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            if (currentPosition.getRow() + 1 <=8) {
                if (currentPosition.getColumn()+1 <= 8) {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow() + 1, col+1);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
                else if (currentPosition.getColumn()+1 <= 8) {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow()+1, currentPosition.getColumn()+1);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
                else {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow()+1, col);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
            }
            else if (currentPosition.getRow()-1 >= 1) {
                if (currentPosition.getColumn()-1 >= 1) {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow()-1, currentPosition.getColumn()-1);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
                else if (currentPosition.getColumn()+1 <= 8) {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow()-1, currentPosition.getColumn()+1);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
                else {
                    ChessPosition mySpot = new ChessPosition(currentPosition.getRow()-1, col);
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                }
            }
            return myMoves;
        }
    }

    private class Knight {
        private final ChessBoard board;
        private final ChessPosition currentPosition;

        private Knight(ChessBoard board, ChessPosition currentPosition) {
            this.board = board;
            this.currentPosition = currentPosition;
        }

        private Collection<ChessMove> knightPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            return myMoves;
        }
        /** while (morePossibleMoves) {
         * if (row+2 <= 8 && col+1 <=8) {
         *     ChessPosition mySpot = new ChessPosition(row+2, col+1);
         *     ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
         *     myMoves.add(myMove);
         * }
         * else if (row+2 <= 8 && col-1 >= 1) {
         *     ChessPosition mySpot = new ChessPosition(row+2, col-1);
         *     ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
         *     myMoves.add(myMove);
         * }
         }
         *
         */
    }
    private class Pawn {
        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private final ChessPiece piece;

        public Pawn(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
            this.board = board;
            this.currentPosition = currentPosition;
            this.piece = piece;
        }

        private Collection<ChessMove> findPawnMoves() {
            int row = currentPosition.getRow();;
            int col = currentPosition.getColumn();;
            Collection<ChessMove> myMoves = new ArrayList<>();
          //  for (int i = 0; i < 3; i++) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (row == 2) {
                        ChessPosition mySpot = new ChessPosition(row+1, col);
                        ChessPosition moveTwo = new ChessPosition(row+2, col);
                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                        ChessMove secondMove = new ChessMove(currentPosition, moveTwo, null);
                        myMoves.add(myMove);
                        myMoves.add(secondMove);
                    }
                    if (row > 2 && row < 7) {
                        ChessPosition mySpot = new ChessPosition(row+1, col);
                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                        myMoves.add(myMove);
                    }
                    if (row == 7) {
                        ChessPosition mySpot = new ChessPosition(row+1, col);
                        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                            switch (pieceType) {
                                case BISHOP:
                                    ChessMove bishopMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP);
                                    myMoves.add(bishopMove);
                                case QUEEN:
                                    ChessMove queenMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN);
                                    myMoves.add(queenMove);
                                case ROOK:
                                    ChessMove rookMove =new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK);
                                    myMoves.add(rookMove);
                                case KNIGHT:
                                    ChessMove knightMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT);
                                    myMoves.add(knightMove);
                            }
                        }

                    }
//                if (board.getPiece() == null)
////                if (row == 2) {
////                    if (row)
//                }
          //      }
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
 * need to be able to identify what color a certain piece blocking the rook is
 *
 *
 * while (board.getPiece
 */