package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws DataAccessException;

    void deleteGames(String gameName) throws DataAccessException;

    GameData getGame(String gameName) throws DataAccessException;

    void createGame(GameData newGame) throws DataAccessException;

    GameData getGameWithID(Integer gameID) throws DataAccessException;

    void addBlackUsername(Integer gameID, String username) throws DataAccessException;

    void addWhiteUsername(Integer gameID, String username) throws DataAccessException;



    }
