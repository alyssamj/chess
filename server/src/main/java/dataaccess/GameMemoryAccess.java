package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GameMemoryAccess implements GameDAO {
    public GameMemoryAccess() {}

    final private HashMap<String, GameData> games = new HashMap<>();
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
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
