package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import spark.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebSocketService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public WebSocketService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        gameDAO.createGame(new GameData(0, null, null, "mygame", new ChessGame()));
    }


    public ChessGame connect(Integer gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGameWithID(gameID);
        ChessGame chessGame = gameData.game();
        return chessGame;
    }

    public String getUsername(String authToken) throws DataAccessException {
        String username = authDAO.returnUserName(authToken);
        return username;
    }
}
