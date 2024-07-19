package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import service.Requests_Responses.*;

import java.util.Collection;
import java.util.Random;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException{
        String authToken = createRequest.authToken();
        String gameName = createRequest.gameName();
        CreateResult createResult;
        if (checkAuthToken(authToken)) {
            createResult = new CreateResult(null, "Error: unauthorized");
            return createResult;
        }
        if (checkGameWithName(gameName)) {
            createResult = new CreateResult(null, "Error: bad request");
            return createResult;
        } else {
            Random random = new Random();
            int newGameID = 0000 + random.nextInt(9999);
            ChessGame newChessBoard = new ChessGame();
            GameData newGame = new GameData(newGameID, null, null, gameName, newChessBoard);
            gameDAO.createGame(newGame);
            createResult = new CreateResult(newGameID, null);
            return createResult;
        }
    }

    public ListResult getListofGames(ListRequest listRequest) throws DataAccessException {
        String authToken = listRequest.authToken();
        if (checkAuthToken(authToken)) {
            ListResult listResult = new ListResult(null, "Error: unauthorized");
            return listResult;
        }
        Collection<GameData> listOfGames = gameDAO.listGames();
        ListResult listResult = new ListResult(listOfGames, null);
        return listResult;
    }

    public JoinResult joinGame(JoinRequest joinRequest) throws DataAccessException {
        String authToken = joinRequest.authToken();
        String playerColor = joinRequest.playerColor();
        if (checkAuthToken(authToken)) {
            JoinResult joinResult = new JoinResult("Error: unauthorized");
            return joinResult;
        }
        String username = authDAO.returnUserName(authToken);
        Boolean potentialGame = checkGameWithID(joinRequest.gameID());
        GameData game = gameDAO.getGameWithID(joinRequest.gameID());
        if (potentialGame == null || !checkPlayerColor(playerColor)) {
            JoinResult joinResult = new JoinResult("Error: bad request");
            return joinResult;
        }
        if (playerColor == "BLACK") {
            if (game.blackUsername() != null) {
                String use = "";
            }
        }

        return null;
    }

    private boolean checkPlayerColor(String playerColor) throws DataAccessException {
        if (playerColor == "BLACK" || playerColor == "WHITE") {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAuthToken(String authToken) throws DataAccessException {
        AuthData auth = authDAO.verifyToken(authToken);
        if (auth == null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkGameWithID(Integer gameID) throws DataAccessException {
        GameData gameToCheck = gameDAO.getGameWithID(gameID);
        if (gameToCheck == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkGameWithName(String gameName) throws DataAccessException {
        GameData gameToCheck = gameDAO.getGame(gameName);
        if (gameToCheck == null) {
            return false;
        } else {
            return true;
        }
    }

}
