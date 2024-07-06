package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    public ChessBoard myBoard = new ChessBoard();
    public TeamColor turn;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        this.myBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (myBoard.getPiece(startPosition) == null) {
            return null;
        } else {
            return myBoard.getPiece(startPosition).pieceMoves(myBoard, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        if (start.getRow() < 1  || start.getRow() > 8 || start.getColumn() <1 || start.getColumn() > 8) {
            throw new InvalidMoveException();
        }
        if (myBoard.getPiece(start) == null) {
            throw new InvalidMoveException();
        }
        if (myBoard.getPiece(start).getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        ChessPiece piece = new ChessPiece(myBoard.getPiece(start).getTeamColor(), myBoard.getPiece(start).getPieceType());
        Collection<ChessMove> availableMoves = new ArrayList<>(piece.pieceMoves(myBoard, start));
        int len = availableMoves.size();
        if (len == 0) {
            throw new InvalidMoveException();
        }
        if (availableMoves.contains(move)) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                ChessPiece promotionPiece = new ChessPiece(turn, move.getPromotionPiece());
                myBoard.addPiece(move.getEndPosition(), promotionPiece);
                myBoard.addPiece(move.getStartPosition(), null);
            } else {
                myBoard.addPiece(move.getEndPosition(), piece);
                myBoard.addPiece(move.getStartPosition(), null);
            }
            myBoard.toString();
            for (TeamColor color : TeamColor.values()) {
                if (color != turn) {
                    setTeamTurn(color);
                }
            }
        } else {
            throw new InvalidMoveException();
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(myBoard, chessGame.myBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(myBoard);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "myBoard=" + myBoard +
                ", turn=" + turn +
                '}';
    }
}
