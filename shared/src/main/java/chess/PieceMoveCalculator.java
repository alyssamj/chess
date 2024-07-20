package chess;

import java.util.*;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition currentPosition;
    private final ChessPiece piece;
    public Collection<ChessMove> myMoves = new ArrayList<>();

    public PieceMoveCalculator(ChessBoard board, ChessPosition currentPosition, ChessPiece piece) {
        this.board = board;
        this.currentPosition = currentPosition;
        this.piece = piece;
    }

    public Collection<ChessMove> getPossibleMoves() {

        switch (piece.getPieceType()) {
            case PAWN:
                Pawn myPawn = new Pawn();
                return myPawn.getPawnMoves();
            case KNIGHT:
                Knight myKnight = new Knight();
                return myKnight.getKnightMoves();
            case BISHOP:
                Bishop myBishop = new Bishop();
                return myBishop.getBishopMoves();
            case KING:
                King myKing = new King();
                return myKing.getKingMoves();
            case ROOK:
                Rook myRook = new Rook();
                return myRook.getRookMoves();
            case QUEEN:
                Queen myQueen = new Queen();
                return myQueen.getQueenMoves();
            default:
                return myMoves;
        }
    }

    private class Pawn {
        Collection<ChessMove> pawnMoves = new ArrayList<>();

        private Pawn() {
        }

        private Collection<ChessMove> getPawnMoves() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            ArrayList<ChessPiece.PieceType> promotionPieces = new ArrayList<>();
            promotionPieces.add(ChessPiece.PieceType.QUEEN);
            promotionPieces.add(ChessPiece.PieceType.KNIGHT);
            promotionPieces.add(ChessPiece.PieceType.BISHOP);
            promotionPieces.add(ChessPiece.PieceType.ROOK);

            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                ChessPosition newSpot = new ChessPosition(row + 1, col);
                ChessPosition attackSpotLeft = new ChessPosition(row + 1, col - 1);
                ChessPosition attackSpotRight = new ChessPosition(row + 1, col + 1);
                if (row == 2) {
                    ChessPosition secondSpot = new ChessPosition(row + 2, col);
                    firstRow(newSpot, secondSpot, attackSpotRight, attackSpotLeft);
                } else if (row > 2 && row < 7) {
                    middleRows(newSpot, attackSpotRight, attackSpotLeft);
                } else if (row == 7) {
                    lastRow(newSpot, attackSpotRight, attackSpotLeft, promotionPieces);
                }
            } else {
                ChessPosition newSpot = new ChessPosition(row - 1, col);
                ChessPosition attackSpotLeft = new ChessPosition(row - 1, col - 1);
                ChessPosition attackSpotRight = new ChessPosition(row - 1, col + 1);
                if (row == 7) {
                    ChessPosition secondSpot = new ChessPosition(row - 2, col);
                    firstRow(newSpot, secondSpot, attackSpotRight, attackSpotLeft);
                } else if (row > 2 && row < 7) {
                    middleRows(newSpot, attackSpotRight, attackSpotLeft);
                } else if (row == 2) {
                    lastRow(newSpot, attackSpotRight, attackSpotLeft, promotionPieces);
                }
            }

            return pawnMoves;
        }

        private boolean checkForTeamColor(ChessPosition moveSpot) {
            return board.getPiece(moveSpot).getTeamColor() != piece.getTeamColor();
        }

        private void lastRow(ChessPosition newSpot, ChessPosition attackSpotRight, ChessPosition attackSpotLeft, ArrayList<ChessPiece.PieceType> promotionPieces) {
            if (board.getPiece(newSpot) == null) {
                for (ChessPiece.PieceType type : promotionPieces) {
                    pawnMoves.add(new ChessMove(currentPosition, newSpot, type));
                }
            }
            if (board.getPiece(attackSpotRight) != null && checkForTeamColor(attackSpotRight)) {
                for (ChessPiece.PieceType type : promotionPieces) {
                    pawnMoves.add(new ChessMove(currentPosition, attackSpotRight, type));
                }
            }
            if (board.getPiece(attackSpotLeft) != null && checkForTeamColor(attackSpotLeft)) {
                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, ChessPiece.PieceType.QUEEN));
                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, ChessPiece.PieceType.BISHOP));
                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, ChessPiece.PieceType.ROOK));
                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, ChessPiece.PieceType.KNIGHT));
            }
        }

        private void middleRows(ChessPosition newSpot, ChessPosition attackSpotRight, ChessPosition attackSpotLeft) {
            if (board.getPiece(newSpot) == null) {
                pawnMoves.add(new ChessMove(currentPosition, newSpot, null));
            }
            if (board.getPiece(attackSpotRight) != null && checkForTeamColor(attackSpotRight)) {
                pawnMoves.add(new ChessMove(currentPosition, attackSpotRight, null));
            }
            if (board.getPiece(attackSpotLeft) != null && checkForTeamColor(attackSpotLeft)) {
                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, null));
            }
        }

        private void firstRow(ChessPosition newSpot, ChessPosition secondSpot, ChessPosition attackSpotRight, ChessPosition attackSpotLeft) {
            if (board.getPiece(newSpot) == null) {
                pawnMoves.add(new ChessMove(currentPosition, newSpot, null));
                if (board.getPiece(secondSpot) == null && board.getPiece(newSpot) == null) {
                    pawnMoves.add(new ChessMove(currentPosition, secondSpot, null));
                }
            }
            if (board.getPiece(attackSpotRight) != null && checkForTeamColor(attackSpotRight)) {
                pawnMoves.add(new ChessMove(currentPosition, attackSpotRight, null));
            }
            if (board.getPiece(attackSpotLeft) != null && checkForTeamColor(attackSpotLeft)) {

                pawnMoves.add(new ChessMove(currentPosition, attackSpotLeft, null));

            }
        }

    }

    private class Knight {
        private Collection<ChessMove> knightPossibleMoves = new ArrayList<>();

        private Knight() {
        }

        private Collection<ChessMove> getKnightMoves() {
            ArrayList<ChessPosition> knightPossibleSpots = new ArrayList<>();
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            knightPossibleSpots.add(new ChessPosition(row + 1, col + 2));
            knightPossibleSpots.add(new ChessPosition(row + 1, col - 2));
            knightPossibleSpots.add(new ChessPosition(row - 1, col + 2));
            knightPossibleSpots.add(new ChessPosition(row - 1, col - 2));
            knightPossibleSpots.add(new ChessPosition(row + 2, col + 1));
            knightPossibleSpots.add(new ChessPosition(row + 2, col - 1));
            knightPossibleSpots.add(new ChessPosition(row - 2, col + 1));
            knightPossibleSpots.add(new ChessPosition(row - 2, col - 1));
            for (ChessPosition possibleSpots : knightPossibleSpots) {
                if (possibleSpots.getColumn() < 1 || possibleSpots.getColumn() > 8 || possibleSpots.getRow() < 1 || possibleSpots.getRow() > 8) {
                    continue;
                }
                if (board.getPiece(possibleSpots) == null) {
                    knightPossibleMoves.add(new ChessMove(currentPosition, possibleSpots, null));
                } else {
                    if (board.getPiece(possibleSpots).getTeamColor() != piece.getTeamColor()) {
                        knightPossibleMoves.add(new ChessMove(currentPosition, possibleSpots, null));
                    }
                }
            }
            return knightPossibleMoves;
        }
    }

    private class King {
        private Collection<ChessMove> kingPossibleMoves = new ArrayList<>();

        private King() {
        }

        private Collection<ChessMove> getKingMoves() {
            ArrayList<ChessPosition> kingPossibleSpots = new ArrayList<>();
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            kingPossibleSpots.add(new ChessPosition(row + 1, col + 1));
            kingPossibleSpots.add(new ChessPosition(row + 1, col - 1));
            kingPossibleSpots.add(new ChessPosition(row + 1, col));
            kingPossibleSpots.add(new ChessPosition(row - 1, col - 1));
            kingPossibleSpots.add(new ChessPosition(row, col + 1));
            kingPossibleSpots.add(new ChessPosition(row - 1, col + 1));
            kingPossibleSpots.add(new ChessPosition(row - 1, col));
            kingPossibleSpots.add(new ChessPosition(row, col - 1));
            for (ChessPosition possibleSpots : kingPossibleSpots) {
                if (possibleSpots.getColumn() < 1 || possibleSpots.getColumn() > 8 || possibleSpots.getRow() < 1 || possibleSpots.getRow() > 8) {
                    continue;
                }
                if (board.getPiece(possibleSpots) == null) {
                    kingPossibleMoves.add(new ChessMove(currentPosition, possibleSpots, null));
                } else {
                    if (board.getPiece(possibleSpots).getTeamColor() != piece.getTeamColor()) {
                        kingPossibleMoves.add(new ChessMove(currentPosition, possibleSpots, null));
                    }
                }
            }
            return kingPossibleMoves;
        }
    }

    private class Bishop {
        Collection<ChessMove> possibleBishopMoves = new ArrayList<>();

        private Bishop() {
        }

        private Collection<ChessMove> getBishopMoves() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            int i;
            int j;
            for (i = row + 1, j = col + 1; i <= 8 && j <= 8; i++, j++) {
                if (addToPossibleMove(i, j)) {
                    break;
                }
            }
            for (i = row - 1, j = col + 1; i >= 1 && j <= 8; i--, j++) {
                if (addToPossibleMove(i, j)) {
                    break;
                }
            }
            for (i = row + 1, j = col - 1; i <= 8 && j >= 1; i++, j--) {
                if (addToPossibleMove(i, j)) {
                    break;
                }
            }
            for (i = row - 1, j = col - 1; i >= 1 && j >= 1; i--, j--) {
                if (addToPossibleMove(i, j)) {
                    break;
                }
            }
            return possibleBishopMoves;
        }

        private boolean addToPossibleMove(int i, int j) {
            ChessPosition nextSpot = new ChessPosition(i, j);
            if (board.getPiece(nextSpot) == null) {
                possibleBishopMoves.add(new ChessMove(currentPosition, nextSpot, null));
            }
            if (board.getPiece(nextSpot) != null) {
                if (board.getPiece(nextSpot).getTeamColor() != piece.getTeamColor()) {
                    possibleBishopMoves.add(new ChessMove(currentPosition, nextSpot, null));
                    return true;
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    private class Rook {
        Collection<ChessMove> rookPossibleMoves = new ArrayList<>();

        private Rook() {
        }

        private Collection<ChessMove> getRookMoves() {
            int row = currentPosition.getRow();
            int col = currentPosition.getColumn();
            int i;
            for (i = row + 1; i <= 8; i++) {
                if (checkRookMove(i, col)) {
                    break;
                }
            }
            for (i = row - 1; i >= 1; i--) {
                if (checkRookMove(i, col)) {
                    break;
                }
            }
            for (i = col - 1; i >= 1; i--) {
                if (checkRookMove(row, i)) {
                    break;
                }
            }
            for (i = col + 1; i <= 8; i++) {
                if (checkRookMove(row, i)) {
                    break;
                }
            }
            return rookPossibleMoves;
        }

        private boolean checkRookMove(int i, int j) {
            ChessPosition nextSpot = new ChessPosition(i, j);
            if (board.getPiece(nextSpot) == null) {
                rookPossibleMoves.add(new ChessMove(currentPosition, nextSpot, null));
            }
            if (board.getPiece(nextSpot) != null) {
                if (board.getPiece(nextSpot).getTeamColor() != piece.getTeamColor()) {
                    rookPossibleMoves.add(new ChessMove(currentPosition, nextSpot, null));
                    return true;
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    private class Queen {
        Collection<ChessMove> possibleQueenMoves = new ArrayList<>();

        private Queen() {
        }

        private Collection<ChessMove> getQueenMoves() {
            Rook queenRook = new Rook();
            Bishop queenBishop = new Bishop();
            possibleQueenMoves.addAll(queenBishop.getBishopMoves());
            possibleQueenMoves.addAll(queenRook.getRookMoves());
            return possibleQueenMoves;
        }

    }

}


