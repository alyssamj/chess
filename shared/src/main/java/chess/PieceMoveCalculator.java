package chess;

import java.util.*;

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
        ChessPiece.PieceType pieceType = piece.getPieceType();
        switch (pieceType) {
            case BISHOP:
                Bishop myBishop = new Bishop(board, currentPosition, piece);
                return myBishop.bishopPossibleDirections();
            case ROOK:
                Rook myRook = new Rook(board, currentPosition, piece);
                return myRook.rookPossibleDirections();
            case KING:
                King myKing = new King(board, currentPosition, piece);
                return myKing.kingPossibleDirections();
            case QUEEN:
                Queen myQueen = new Queen(board, currentPosition, piece);
                return myQueen.queenPossibleDirections();
            case KNIGHT:
                Knight myKnight = new Knight(board, currentPosition, piece);
                return myKnight.knightPossibleDirections();
            case PAWN:
                Pawn myPawn = new Pawn(board, currentPosition, piece);
                return myPawn.findPawnMoves();
            default:
                return Collections.EMPTY_LIST;
        }
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
        private final ChessPiece piece;
        private final ChessPosition currentPosition;

        private Bishop(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
        //    super();
            this.board = board;
            this.currentPosition = currentPosition;
            this.piece = piece;
        }

        private Collection<ChessMove> bishopPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();
            int i = 0;
            int j = 0;
            for (i = row+1, j = col+1; i <= 8 && j <= 8; i++, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessPosition spotOnBoard = new ChessPosition(i, j);
                if (mySpot.getRow() < 1 || mySpot.getRow() > 8 || mySpot.getColumn() < 1 || mySpot.getColumn() > 8) {
                    continue;
                }
                if (board.getPiece(spotOnBoard) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (piece.getTeamColor().equals(board.getPiece(spotOnBoard).getTeamColor())) {
                    break;
                }  else {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                }
//                if (!piece.getTeamColor().equals(board.getPiece(mySpot).getTeamColor())) {
//                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                    myMoves.add(myMove);
//                    break;
//                }
//                if (piece.getTeamColor().equals(board.getPiece(mySpot).getTeamColor())) {
//                    break;
////                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
////                    myMoves.add(myMove);
//                }
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
            }
            for (i = row-1, j = col-1; i >=1 && j >=1; i--, j--) {
//                ChessPosition mySpot = new ChessPosition(i, j);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessPosition spotOnBoard = new ChessPosition(i, j);
                if (mySpot.getRow() < 1 || mySpot.getRow() > 8 || mySpot.getColumn() < 1 || mySpot.getColumn() > 8) {
                    continue;
                }
                if (board.getPiece(spotOnBoard) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (piece.getTeamColor().equals(board.getPiece(spotOnBoard).getTeamColor())) {
                    break;
                } else {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                }
            }
            for (i = row+1, j = col-1; i <=8 && j >= 1; i++, j--) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessPosition spotOnBoard = new ChessPosition(i, j);
                if (mySpot.getRow() < 1 || mySpot.getRow() > 8 || mySpot.getColumn() < 1 || mySpot.getColumn() > 8) {
                    continue;
                }
                if (board.getPiece(spotOnBoard) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (piece.getTeamColor().equals(board.getPiece(spotOnBoard).getTeamColor())) {
                    break;
                } else {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                }
            }
            for (i = row-1, j = col+1; i >= 1 && j <= 8; i--, j++) {
                ChessPosition mySpot = new ChessPosition(i, j);
                ChessPosition spotOnBoard = new ChessPosition(i, j);
                if (mySpot.getRow() < 1 || mySpot.getRow() > 8 || mySpot.getColumn() < 1 || mySpot.getColumn() > 8) {
                    continue;
                }
                if (board.getPiece(spotOnBoard) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (piece.getTeamColor().equals(board.getPiece(spotOnBoard).getTeamColor())) {
                    break;
                } else {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                }
            }
            return myMoves;
        }
    }
    private class Queen{
        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private Collection<ChessMove> possibleMoves;
        private final ChessPiece piece;
        private Bishop myBishop;
        private Rook myRook;


        private Queen(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
            //super();
            this.board = board;
            this.piece = piece;
            this.currentPosition = currentPosition;
            this.myBishop = new Bishop(board, currentPosition, piece);
            this.myRook = new Rook(board, currentPosition, piece);
        }

        private Collection<ChessMove> queenPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();
            myMoves.addAll(myBishop.bishopPossibleDirections());
            myMoves.addAll(myRook.rookPossibleDirections());

//            for (int i = col+1; i <= 8; ++i) {
//                ChessPosition mySpot = new ChessPosition(row, i);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
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
//            int i = 0;
//            int j = 0;
//            for (i = row+1, j = col+1; i <=8 && j <= 8; i++, j++) {
//                ChessPosition mySpot = new ChessPosition(i, j);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            for (i = row-1, j = col-1; i >=1 && j >= 1; i--, j--) {
//                ChessPosition mySpot = new ChessPosition(i, j);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            for (i = row+1, j = col-1; i <=8 && j >= 1; i++, j--) {
//                ChessPosition mySpot = new ChessPosition(i, j);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
//            for (i = row-1, j = col+1; i >=1 && j <= 8; i--, j++) {
//                ChessPosition mySpot = new ChessPosition(i, j);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
//            }
            return myMoves;
        }

}
    private class Rook{ //extends PieceMoveCalculator {

        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private final ChessPiece piece;
        private Collection<ChessMove> possibleMoves;

        private Rook(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
            //super();
            this.board = board;
            this.piece = piece;
            this.currentPosition = currentPosition;
        }

        private Collection<ChessMove> rookPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            //
            for (int i = col+1; i <= 8 && row >= 1 && row <= 8; ++i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                if (board.getPiece(mySpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (board.getPiece(mySpot).getTeamColor() != piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                } else if (board.getPiece(mySpot).getTeamColor() == piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    break;
                }
            }
            for (int i = col-1; i >= 1 && row <=8 && row >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(row, i);
                if (board.getPiece(mySpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (board.getPiece(mySpot).getTeamColor() != piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                } else if (board.getPiece(mySpot).getTeamColor() == piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    break;
                }
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
            }
            for (int i = row+1; i <= 8 && col >= 1 && col <= 8; ++i) {
                ChessPosition mySpot = new ChessPosition(i, col);
                if (board.getPiece(mySpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (board.getPiece(mySpot).getTeamColor() != piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                } else if (board.getPiece(mySpot).getTeamColor() == piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    break;
                }
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
            }
            for (int i = row-1; i >= 1 && col <= 8 && col >= 1; --i) {
                ChessPosition mySpot = new ChessPosition(i, col);
//                ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
//                myMoves.add(myMove);
                if (board.getPiece(mySpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                } else if (board.getPiece(mySpot).getTeamColor() != piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    myMoves.add(myMove);
                    break;
                } else if (board.getPiece(mySpot).getTeamColor() == piece.getTeamColor()) {
                    ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                    break;
                }
            }
            return myMoves;
        }
    }
    private class King { //extends PieceMoveCalculator {

        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private final ChessPiece piece;
        private Collection<ChessMove> possibleMoves;

        private King(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
            //super();
            this.board = board;
            this.currentPosition = currentPosition;
            this.piece = piece;
        }
        private Collection<ChessMove> kingPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            //for (int i = 0; i < 8; i++) {
                if (row+1 < 8) {
                    ChessPosition firstSpot = new ChessPosition(row + 1, col);
                    if (board.getPiece(firstSpot) == null) {
                        ChessMove myMove = new ChessMove(currentPosition, firstSpot, null);
                        myMoves.add(myMove);
                    } else {
                        if (board.getPiece(firstSpot).getTeamColor() != piece.getTeamColor()) {
                            myMoves.add(new ChessMove(currentPosition, firstSpot, null));
                        }
                    }
                    if (col-1 >= 1) {
                        ChessPosition secondSpot = new ChessPosition(row + 1, col-1);
                        if (board.getPiece(secondSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, secondSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(secondSpot).getTeamColor() != piece.getTeamColor()) {
                                //ChessMove myMove = new ChessMove(currentPosition, secondSpot, null);
                                myMoves.add(new ChessMove(currentPosition, secondSpot, null));
                            }
                        }
                        ChessPosition fourthSpot = new ChessPosition(row, col-1);
                        if (board.getPiece(fourthSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, fourthSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(fourthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, fourthSpot, null));
                            }
                        }
                    }
                    if (col+1 <= 8) {
                        ChessPosition thirdSpot = new ChessPosition(row + 1, col + 1);
                        if (board.getPiece(thirdSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, thirdSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(thirdSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, thirdSpot, null));
                            }
                        }
                        ChessPosition fifthSpot = new ChessPosition(row, col+1);
                        if (board.getPiece(fifthSpot)==null) {
                            ChessMove myMove = new ChessMove(currentPosition, fifthSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(fifthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, fifthSpot, null));
                            }
                        }
                    }
                }
                if (row > 1) {
                    if (col+1 <= 8) {
                        ChessPosition sixthSpot = new ChessPosition(row-1, col+1);
                        if (board.getPiece(sixthSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, sixthSpot, null);
                            myMoves.add(myMove);
                        }else {
                            if (board.getPiece(sixthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, sixthSpot, null));
                            }
                        }
                    }
                    if (col > 1) {
                        ChessPosition seventhSpot = new ChessPosition(row-1, col);
                        if (board.getPiece(seventhSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, seventhSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(seventhSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, seventhSpot, null));
                            }
                        }
                        ChessPosition eighthSpot = new ChessPosition(row-1, col-1);
                        if (board.getPiece(eighthSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, eighthSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(eighthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, eighthSpot, null));
                            }
                        }
                    }
                }
            return myMoves;
        }
    }
    private class Knight {
        private final ChessBoard board;
        private final ChessPosition currentPosition;
        private final ChessPiece piece;

        private Knight(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
            this.board = board;
            this.currentPosition = currentPosition;
            this.piece = piece;
        }

        private Collection<ChessMove> knightPossibleDirections() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();
            if (row + 2 <= 8 && col + 1 <= 8) {
                ChessPosition firstSpot = new ChessPosition(row + 2, col + 1);
                if (board.getPiece(firstSpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, firstSpot, null);
                    myMoves.add(myMove);
                } else {
                    if (board.getPiece(firstSpot).getTeamColor() != piece.getTeamColor()) {
                        myMoves.add(new ChessMove(currentPosition, firstSpot, null));
                    }
                }
            }
            if (row + 2 <= 8 && col - 1 >= 1) {
                ChessPosition secondSpot = new ChessPosition(row + 2, col - 1);
                if (board.getPiece(secondSpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, secondSpot, null);
                    myMoves.add(myMove);
                } else {
                    if (board.getPiece(secondSpot).getTeamColor() != piece.getTeamColor()) {
                        //ChessMove myMove = new ChessMove(currentPosition, secondSpot, null);
                        myMoves.add(new ChessMove(currentPosition, secondSpot, null));
                    }
                }
            }
            if (row + 1 <= 8 && col + 2 <= 8) {
                ChessPosition fourthSpot = new ChessPosition(row + 1, col + 2);
                if (board.getPiece(fourthSpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, fourthSpot, null);
                    myMoves.add(myMove);
                } else {
                    if (board.getPiece(fourthSpot).getTeamColor() != piece.getTeamColor()) {
                        myMoves.add(new ChessMove(currentPosition, fourthSpot, null));
                    }
                }
            }

            if (row + 1 <= 8 && col - 2 >= 1) {
                ChessPosition thirdSpot = new ChessPosition(row + 1, col - 2);
                if (board.getPiece(thirdSpot) == null) {
                    ChessMove myMove = new ChessMove(currentPosition, thirdSpot, null);
                    myMoves.add(myMove);
                } else {
                    if (board.getPiece(thirdSpot).getTeamColor() != piece.getTeamColor()) {
                        myMoves.add(new ChessMove(currentPosition, thirdSpot, null));
                    }
                }
            }
                if (row - 1 >= 1 && col + 2 <= 8) {
                    ChessPosition fifthSpot = new ChessPosition(row - 1, col + 2);
                    if (board.getPiece(fifthSpot) == null) {
                        ChessMove myMove = new ChessMove(currentPosition, fifthSpot, null);
                        myMoves.add(myMove);
                    } else {
                        if (board.getPiece(fifthSpot).getTeamColor() != piece.getTeamColor()) {
                            myMoves.add(new ChessMove(currentPosition, fifthSpot, null));
                        }
                    }
                }
                    if (row - 1 >= 1 && col - 2 >= 1) {
                        ChessPosition sixthSpot = new ChessPosition(row - 1, col - 2);
                        if (board.getPiece(sixthSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, sixthSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(sixthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, sixthSpot, null));
                            }
                        }
                    }
                    if (row - 2 >= 1 && col + 1 <= 8) {
                        ChessPosition seventhSpot = new ChessPosition(row - 2, col + 1);
                        if (board.getPiece(seventhSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, seventhSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(seventhSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, seventhSpot, null));
                            }
                        }
                    }
                    if (row - 2 >= 1 && col - 1 >= 1) {
                        ChessPosition eighthSpot = new ChessPosition(row - 2, col - 1);
                        if (board.getPiece(eighthSpot) == null) {
                            ChessMove myMove = new ChessMove(currentPosition, eighthSpot, null);
                            myMoves.add(myMove);
                        } else {
                            if (board.getPiece(eighthSpot).getTeamColor() != piece.getTeamColor()) {
                                myMoves.add(new ChessMove(currentPosition, eighthSpot, null));
                            }
                        }
                    }
            return myMoves;
            }
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
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            Collection<ChessMove> myMoves = new ArrayList<>();

            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    if (row == 2) {
                        ChessPosition mySpot = new ChessPosition(row + 1, col);
                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                        myMoves.add(myMove);
                        ChessPosition moveTwo = new ChessPosition(row + 2, col);
                        if (board.getPiece(moveTwo) == null) {
                            ChessMove secondMove = new ChessMove(currentPosition, moveTwo, null);
                            myMoves.add(secondMove);
                        }
                    }
                    if (row > 2 && row < 7) {
                        ChessPosition mySpot = new ChessPosition(row + 1, col);
                        ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                        myMoves.add(myMove);
                    }

                    if (row == 7) {
                        ChessPosition mySpot = new ChessPosition(row + 1, col);
                        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                            switch (pieceType) {
                                case BISHOP:
                                    ChessMove bishopMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP);
                                    myMoves.add(bishopMove);
                                case QUEEN:
                                    ChessMove queenMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN);
                                    myMoves.add(queenMove);
                                case ROOK:
                                    ChessMove rookMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK);
                                    myMoves.add(rookMove);
                                case KNIGHT:
                                    ChessMove knightMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT);
                                    myMoves.add(knightMove);
                            }
                        }
                    }
                }
                if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                    if (board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != piece.getTeamColor()) {
                        ChessPosition mySpot = new ChessPosition(row + 1, col + 1);
                        if (row == 7) {
                            for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                                switch (pieceType) {
                                    case BISHOP:
                                        ChessMove bishopMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP);
                                        myMoves.add(bishopMove);
                                    case QUEEN:
                                        ChessMove queenMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN);
                                        myMoves.add(queenMove);
                                    case ROOK:
                                        ChessMove rookMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK);
                                        myMoves.add(rookMove);
                                    case KNIGHT:
                                        ChessMove knightMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT);
                                        myMoves.add(knightMove);
                                }
                            }
                        } else {
                            myMoves.add(new ChessMove(currentPosition, mySpot, null));
                        }
                    }
                }
                if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                    if (board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != piece.getTeamColor()) {
                        ChessPosition mySpot = new ChessPosition(row + 1, col - 1);
                        if (row == 7) {
                            for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                                switch (pieceType) {
                                    case BISHOP:
                                        ChessMove bishopMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP);
                                        myMoves.add(bishopMove);
                                    case QUEEN:
                                        ChessMove queenMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN);
                                        myMoves.add(queenMove);
                                    case ROOK:
                                        ChessMove rookMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK);
                                        myMoves.add(rookMove);
                                    case KNIGHT:
                                        ChessMove knightMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT);
                                        myMoves.add(knightMove);
                                }
                            }
                        } else {
                            myMoves.add(new ChessMove(currentPosition, mySpot, null));
                        }
                    }
                }
            }
            else {
                    if (board.getPiece(new ChessPosition(row-1, col)) == null) {
                        if (row == 7) {
                            ChessPosition firstSpot = new ChessPosition(row - 1, col);
                            ChessMove firstMove = new ChessMove(currentPosition, firstSpot, null);
                            myMoves.add(firstMove);
                            ChessPosition secondSpot = new ChessPosition(row - 2, col);
                            if (board.getPiece(secondSpot) == null) {
                                ChessMove secondMove = new ChessMove(currentPosition, secondSpot, null);
                                myMoves.add(secondMove);
                            }
                        }
                        if (row > 2 && row < 7) {
                            ChessPosition mySpot = new ChessPosition(row - 1, col);
                            ChessMove myMove = new ChessMove(currentPosition, mySpot, null);
                            myMoves.add(myMove);
                        }
                        if (row == 2) {
                            ChessPosition mySpot = new ChessPosition(row - 1, col);
                            for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                                switch (pieceType) {
                                    case QUEEN:
                                        ChessMove queenMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN);
                                        myMoves.add(queenMove);
                                    case ROOK:
                                        ChessMove rookMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK);
                                        myMoves.add(rookMove);
                                    case BISHOP:
                                        ChessMove bishopMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP);
                                        myMoves.add(bishopMove);
                                    case KNIGHT:
                                        ChessMove knightMove = new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT);
                                        myMoves.add(knightMove);
                                }
                            }
                        }
                    }
                    if (board.getPiece(new ChessPosition(row-1, col-1)) != null) {
                        ChessPosition mySpot = new ChessPosition(row-1, col-1);
                        if (row == 2) {
                            for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                                switch (pieceType){
                                    case KNIGHT :
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT));
                                    case QUEEN:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN));
                                    case ROOK:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK));
                                    case BISHOP:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP));
                                }
                            }
                        } else {
                            myMoves.add(new ChessMove(currentPosition, mySpot, null));
                        }
                    }
                    if (board.getPiece(new ChessPosition(row-1, col+1)) != null) {
                        ChessPosition mySpot = new ChessPosition(row-1, col+1);
                        if (row == 2) {
                            for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
                                switch (pieceType) {
                                    case ROOK:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.ROOK));
                                    case QUEEN:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.QUEEN));
                                    case BISHOP:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.BISHOP));
                                    case KNIGHT:
                                        myMoves.add(new ChessMove(currentPosition, mySpot, ChessPiece.PieceType.KNIGHT));
                                }
                            }
                        }
                        else {
                            myMoves.add(new ChessMove(currentPosition, mySpot, null));
                        }
                    }

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