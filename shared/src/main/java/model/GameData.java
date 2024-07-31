package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameData getGame() {
        return this;
    }
}
