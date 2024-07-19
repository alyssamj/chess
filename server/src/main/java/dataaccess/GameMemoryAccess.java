package dataaccess;

import chess.*;
import model.*;

import java.util.*;

public class GameMemoryAccess implements GameDAO {
    public GameMemoryAccess() {
    }

    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        for (Map.Entry<Integer, GameData> game : games.entrySet()) {
            gameList.add(game.getValue());
        }
        return gameList;
    }

    @Override
    public void deleteGames(String gameName) throws DataAccessException {

    }
    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        for (Map.Entry<Integer, GameData> game : games.entrySet()) {
            if (game.getValue().gameName() == gameName) {
                return game.getValue();
            }
        }
        return null;
    }
    @Override
    public GameData getGameWithID(Integer gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        }
        return null;
    }
    @Override
    public void createGame(GameData newGame) throws DataAccessException {
        games.put(newGame.gameID(), newGame);
    }
    @Override
    public void addBlackUsername(Integer gameID, String username) throws DataAccessException{
        GameData gameToChange = games.get(gameID);
        String gameName = gameToChange.gameName();
        String blackUserName = username;
        String whiteUserName = gameToChange.whiteUsername();
        ChessGame game = gameToChange.game();
        GameData updatedGame = new GameData(gameID, whiteUserName, blackUserName, gameName, game);
        games.put(gameID, updatedGame);
    }
    @Override
    public void addWhiteUsername(String username) throws DataAccessException {

    }

    public GameData updateGame(Integer gameID, String username, String playerColor) throws DataAccessException {
        GameData gameToChange = games.get(gameID);
        String gameName = gameToChange.gameName();


    }

}
