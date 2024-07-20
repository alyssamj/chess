package service.Requests_Responses;

import model.GameData;

import java.util.Collection;

public record ListResult(Collection<GameData> gameList, String message) {
    public Collection<GameData> getGames() {
        return gameList();
    }
}
