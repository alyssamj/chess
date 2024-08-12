package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
      //  gameDAO.createGame(new GameData(0, null, null, "mygame", new ChessGame()));
    }


    public ChessGame connect(Integer gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGameWithID(gameID);
        ChessGame chessGame = gameData.game();
        return chessGame;
    }

    public String makeMove(ChessMove move, Integer gameID) throws DataAccessException {
        GameData game = gameDAO.getGameWithID(gameID);
        ChessGame chessGame = game.game();
        try {
            chessGame.makeMove(move);
        } catch (InvalidMoveException e) {
            return "Unable to make move";
        }
        return "Move successful";
    }

    public String leaveGame(Integer gameID, String username) throws DataAccessException {
        GameData game = gameDAO.getGameWithID(gameID);
        if (gameDAO.getWhiteUsername(gameID) == null || gameDAO.getBlackUsername(gameID) == null){
            return "";
            }
        else if (gameDAO.getWhiteUsername(gameID).equals(username)){
                gameDAO.clearWhiteUsername(gameID);
        }
        else if (gameDAO.getBlackUsername(gameID).equals(username)) {
            gameDAO.clearBlackUsername(gameID);
        }
        return "";
    }

    public void resign(Integer gameID) throws DataAccessException {
        GameData game = gameDAO.getGameWithID(gameID);
        game.game().setState(ChessGame.GameState.GAME_OVER);
        gameDAO.updateGame(gameID, game.game());
    }




    public String getUsername(String authToken) throws DataAccessException {
        String username = authDAO.returnUserName(authToken);
        return username;
    }

    public boolean getGameID(int gameID) {
        try {
            if (gameDAO.getGameWithID(gameID) == null) {
                return false;
            } else {
                return true;
            }
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean getAuth(String authToken) {
        try {
            if (authDAO.verifyToken(authToken) == null) {
                return false;
            } else {
                return true;
            }
        } catch (DataAccessException e) {
            return false;
        }
    }
}
