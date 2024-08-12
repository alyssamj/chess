package dataaccess;

import chess.ChessGame;
import model.*;

public interface GameDAO {
    GameData[] listGames() throws DataAccessException;

    boolean clear() throws DataAccessException;

    GameData getGameWithGameName(String gameName) throws DataAccessException;

    void createGame(GameData newGame) throws DataAccessException;

    GameData getGameWithID(Integer gameID) throws DataAccessException;

    void addBlackUsername(Integer gameID, String username) throws DataAccessException;

    void addWhiteUsername(Integer gameID, String username) throws DataAccessException;

    String getBlackUsername(Integer gameID) throws DataAccessException;

    String getWhiteUsername(Integer gameID) throws DataAccessException;

    void clearBlackUsername(Integer gameID) throws DataAccessException;

    void clearWhiteUsername(Integer gameID) throws DataAccessException;

    void updateGame(Integer gameID, ChessGame game) throws DataAccessException;
}
