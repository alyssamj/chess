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

    public WebSocketService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }


    public ChessGame connect(Integer gameID) throws DataAccessException {
        GameData gamData = gameDAO.getGameWithID(gameID);
        ChessGame chessGame = gamData.game();
        return chessGame;
    }
}
