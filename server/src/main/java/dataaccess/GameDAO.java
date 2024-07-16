package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws DataAccessException;

    void deleteGames() throws DataAccessException;

}
