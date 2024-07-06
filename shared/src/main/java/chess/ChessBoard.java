package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] spots = new ChessPiece[8][8];

    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = null;
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        spots[row][col] = piece;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spots.length; i++) {
            for (int j = 0; j < spots[i].length; j++) {
                if (spots[i][j] == null) {
                    sb.append(" ");
                } else {
                    sb.append(spots[i][j].getPieceType().toString().charAt(0));
                }
                if (j < spots[i].length - 1) {
                    sb.append("|");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        if (row >= 8 || col >= 8 || row < 0 || col < 0) {
            return null;
        }
//        if (spots[row][col] == null) {
//            return null;
//        }
        return spots[row][col];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(spots, that.spots);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(spots);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = null;
            }
        }
        this.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        for (int i = 1; i <=8; i++) {
            this.addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        this.addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        for (int i = 1; i <=8; i++) {
            this.addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }
}
