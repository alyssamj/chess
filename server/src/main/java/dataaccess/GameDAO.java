package dataaccess;

import model.*;

public interface GameDAO {
    GameData[] listGames() throws DataAccessException;

    boolean clear() throws DataAccessException;

    GameData getGameWithGameName(String gameName) throws DataAccessException;

    void createGame(GameData newGame) throws DataAccessException;

    GameData getGameWithID(Integer gameID) throws DataAccessException;

    void addBlackUsername(Integer gameID, String username) throws DataAccessException;

    void addWhiteUsername(Integer gameID, String username) throws DataAccessException;



    }
