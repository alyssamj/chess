package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private ChessMove move;

    public MakeMove(ChessMove move, String authToken, Integer gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
