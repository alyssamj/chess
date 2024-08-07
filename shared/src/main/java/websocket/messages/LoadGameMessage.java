package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    public final Object game;

    public LoadGameMessage(Object game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public Object getGame() {
        return game;
    }
}
