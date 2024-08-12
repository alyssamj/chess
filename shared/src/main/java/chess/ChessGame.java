package chess;

import java.util.ArrayList;
import java.util.Collection;
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
    public GameState state;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        this.myBoard.resetBoard();
        this.state = GameState.IN_PROGRESS;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    public enum GameState {
        GAME_OVER,
        IN_PROGRESS
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
        Collection<ChessMove> myValidMoves = new ArrayList<>();
        if (myBoard.getPiece(startPosition) == null) {
            return myValidMoves;
        }
        ChessPiece piece = myBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = piece.pieceMoves(this.myBoard, startPosition);
        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = new ChessBoard(this.myBoard);
            this.myBoard = new ChessBoard(tempBoard);
            testMoves(move);
            if (!isInCheck(piece.getTeamColor())) {
                myValidMoves.add(move);
            }
            this.myBoard = new ChessBoard(tempBoard);
        }

        return myValidMoves;
    }
    public void testMoves(ChessMove move) {
        ChessPosition start = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        if (myBoard.getPiece(start) != null) {
            addMove(start, move);
        }
    }

    private void addMove(ChessPosition start, ChessMove move) {
        ChessPiece piece = new ChessPiece(myBoard.getPiece(start).getTeamColor(), myBoard.getPiece(start).getPieceType());
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            ChessPiece promotionPiece = new ChessPiece(turn, move.getPromotionPiece());
            myBoard.addPiece(move.getEndPosition(), promotionPiece);
            myBoard.addPiece(move.getStartPosition(), null);
        } else {
            myBoard.addPiece(move.getEndPosition(), piece);
            myBoard.addPiece(move.getStartPosition(), null);
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
        if (start.getRow() < 1 || start.getRow() > 8 || start.getColumn() < 1 || start.getColumn() > 8) {
            throw new InvalidMoveException();
        }
        if (myBoard.getPiece(start) == null || myBoard.getPiece(start).getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> allPossibleMoves = validMoves(start);
        if (!allPossibleMoves.contains(move)) {
            throw new InvalidMoveException();
        } else {
            addMove(start, move);
            for (TeamColor color : TeamColor.values()) {
                if (color != turn) {
                    setTeamTurn(color);
                    break;
                }
            }
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
                    return kingPosition;
                }
            }
        }
        return kingPosition;
    }

    private boolean checkKingTakesPieces(TeamColor teamColor) {
        ChessPosition teamKingPosition = calculateOwnKingSpot(teamColor);
        Collection<ChessMove> potentialKingTakers = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition newSpot = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(newSpot);
                if (piece == null || piece.getTeamColor() == teamColor) {
                    continue;
                }
                Collection<ChessMove> pieceMoves = piece.pieceMoves(myBoard, newSpot);
                for (ChessMove move : pieceMoves) {
                    if (checkPotentialKingTakers(move, teamKingPosition)) {
                        potentialKingTakers.add(move);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkPotentialKingTakers(ChessMove myMove, ChessPosition kingPosition) {
        if (myMove.getEndPosition().equals(kingPosition)) {
            return true;
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return checkKingTakesPieces(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> possibleMoves = isInStaleOrCheck(teamColor);
        if (possibleMoves.isEmpty()) {
            setState(GameState.GAME_OVER);
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> possibleMoves = isInStaleOrCheck(teamColor);
        if (possibleMoves.isEmpty()) {
            setState(GameState.GAME_OVER);
            return true;
        }
        return false;
    }


    private Collection<ChessMove> isInStaleOrCheck(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j<=8; j++) {
                ChessPosition newSpot = new ChessPosition(i, j);
                ChessPiece piece = myBoard.getPiece(newSpot);
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }
                Collection<ChessMove> moves = piece.pieceMoves(myBoard, newSpot);
                for (ChessMove move : moves) {
                    possibleMoves.addAll(validMoves(move.getStartPosition()));
                }
            }
        }
        return possibleMoves;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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