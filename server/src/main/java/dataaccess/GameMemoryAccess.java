package dataaccess;

import model.*;

import java.util.*;

public class GameMemoryAccess implements GameDAO {
    public GameMemoryAccess() {
    }

    final private HashMap<String, GameData> games = new HashMap<>();
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        for (Map.Entry<String, GameData> game : games.entrySet()) {
            gameList.add(game.getValue());
        }
        return gameList;
    }

    @Override
    public void deleteGames(String gameName) throws DataAccessException {

    }
    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        if (games.containsKey(gameName)) {
            return games.get(gameName);
        }
        return null;
    }
    @Override
    public void createGame(GameData newGame) throws DataAccessException {
        games.put(newGame.gameName(), newGame);
    }
}
