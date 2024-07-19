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
        String playerColor = "BLACK";
        GameData updatedGame = updateGame(gameID, username, playerColor);
        games.put(gameID, updatedGame);
    }
    @Override
    public void addWhiteUsername(Integer gameID, String username) throws DataAccessException {
        String playerColor = "WHITE";
        GameData updatedGame = updateGame(gameID, username, playerColor);
        games.put(gameID, updatedGame);
    }

    public GameData updateGame(Integer gameID, String username, String playerColor) throws DataAccessException {
        GameData gameToChange = games.get(gameID);
        String gameName = gameToChange.gameName();
        ChessGame game = gameToChange.game();
        if (playerColor == "BLACK") {
            String blackUserName = username;
            String whiteUserName = gameToChange.whiteUsername();
            GameData updatedGame = new GameData(gameID, whiteUserName, blackUserName, gameName, game);
            return updatedGame;
        } else {
            String whiteUserName = username;
            String blackUserName = gameToChange.blackUsername();
            GameData updatedGame = new GameData(gameID, whiteUserName, blackUserName, gameName, game);
            return updatedGame;
        }
    }

}
