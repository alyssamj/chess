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
        }
        ChessBoard tempBoard;
        tempBoard = new ChessBoard(myBoard);
        this.myBoard = new ChessBoard(tempBoard);
        //   copyOfBoard.spots[5][5] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING);
        try {
            this.makeMove(new ChessMove(startPosition, new ChessPosition(5, 5), null));
        } catch (InvalidMoveException e) {
            System.out.println("Not valid ALYSSA");
        }
        this.myBoard = new ChessBoard(tempBoard);
//        Collection<ChessPiece> allPieces = new ArrayList<>();
//        Collection<ChessPiece> potentialKingTakers = new ArrayList<>();
//        ChessPosition kingPosition = new ChessPosition(0, 0);
//        for (int i = 1; i <=8; i++) {
//            for (int j = 1; j <=8; j++) {
//                ChessPosition newSpot = new ChessPosition(i, j);
//                ChessPiece piece = myBoard.getPiece(newSpot);
//                if (piece != null) {
//                    allPieces.add(piece);
//                }
//                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == turn) {
//                    kingPosition = newSpot;
//                }
//            }
//        }
//        for (ChessPiece piece : allPieces) {
//            Collection<ChessMove> pieceMoves = piece.pieceMoves(myBoard,);
//            if (piece.getTeamColor() != turn) {
//                for (ChessMove move : pieceMoves) {
//                    if (move.getEndPosition() == kingPosition) {
//
//                    }
//                }
//            }
//        }
        return myBoard.getPiece(startPosition).pieceMoves(myBoard, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        if (start.getRow() < 1 || start.getRow() > 8 || start.getColumn() < 1 || start.getColumn() > 8) {
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
        if (availableMoves.isEmpty()) {
            throw new InvalidMoveException();
        }
        if (isInCheck(turn)) {
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
    private ChessPosition calculateOwnKingSpot(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(1, 1);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition newSpot = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(newSpot);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPosition = newSpot;
                    break;
                }
            }
        }
        return kingPosition;
    }

    private ChessPosition calculateEnemyKingSpot(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(1, 1);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition newSpot = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(newSpot);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() != teamColor) {
                    kingPosition = newSpot;
                    break;
                }
            }
        }
        return kingPosition;
    }


    private Collection<ChessMove> checkAllPieces(TeamColor teamColor) {
        Collection<ChessPiece> allPieces = new ArrayList<>();
        Collection<ChessMove> potentialKingTakers = new ArrayList<>();
        ChessPosition kingPosition = calculateEnemyKingSpot(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition newSpot = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(newSpot);
                if (piece != null) {
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(myBoard, newSpot);
                    for (ChessMove move : pieceMoves) {
                        if (move.getEndPosition() == kingPosition && myBoard.getPiece(move.getStartPosition()).getTeamColor() != teamColor) {
                            potentialKingTakers.add(move);
                        }
                    }
                }
            }
        }
//        for (ChessPiece piece : allPieces) {
//            Collection<ChessMove> pieceMoves = piece.pieceMoves(myBoard, myBoard.);
//            if (piece.getTeamColor() != turn) {
//                for (ChessMove move : pieceMoves) {
//                    if (move.getEndPosition() == kingPosition) {
//                        potentialKingTakers.add(piece);
//                    }
//                }
//            }
//        }
        return potentialKingTakers;
    }

    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> possibleTeamMoves = new ArrayList<>();
        Collection<ChessMove> possibleEnemyMoves = new ArrayList<>();
        Collection<ChessPiece> potentialChecks = new ArrayList<>();
        ChessPosition kingPosition = calculateOwnKingSpot(teamColor);
        possibleEnemyMoves = checkAllPieces(teamColor);
        if (possibleEnemyMoves.isEmpty()) {
            return false;
        }

//        for (int i = 1; i <= 8; i++) {
//            for (int j = 1; j <= 8; j++) {
//                ChessPosition spot = new ChessPosition(i, j);
//                ChessPiece myPiece = myBoard.getPiece(spot);
//                if (myPiece != null && myPiece.getTeamColor() == turn) {
//                    possibleTeamMoves.addAll(myPiece.pieceMoves(myBoard, spot));
//                } else if (myPiece != null && myPiece.getTeamColor() != turn) {
//                    possibleEnemyMoves.addAll(myPiece.pieceMoves(myBoard, spot));
//                }
//            }
//        }
        return true;
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
        return false;
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

    private class DeepCopy {
        ChessBoard copyOfBoard;

        private DeepCopy(ChessBoard board) {
            this.copyOfBoard = new ChessBoard(board);
        }

        private ChessBoard getCopyOfBoard() {
            return copyOfBoard;
        }

    }
}
