package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws DataAccessException;

    void deleteGames(String gameName) throws DataAccessException;

    GameData getGame(String gameName) throws DataAccessException;

    void createGame(String gameName, Integer gameID) throws DataAccessException;



    }
