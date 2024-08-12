package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private ChessMove move;
    private ChessGame.TeamColor teamColor;

    public MakeMove(ChessMove move, String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.teamColor = teamColor;
    }

    public ChessMove getMove() {
        return move;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
