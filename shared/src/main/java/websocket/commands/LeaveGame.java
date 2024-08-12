package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class LeaveGame extends UserGameCommand{
   public ChessGame.TeamColor teamColor;

    public LeaveGame(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.LEAVE, authToken, gameID);
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
