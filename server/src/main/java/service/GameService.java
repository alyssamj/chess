package service;

import requestsandresponses.*;
import chess.ChessGame;
import dataaccess.*;
import model.*;


import java.util.Random;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private int gameID = 1;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException{
        GameData[] games = gameDAO.listGames();
        for (int i = 0; i < games.length; i++) {
            gameID++;
        }
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
            ChessGame newChessBoard = new ChessGame();
            GameData newGame = new GameData(gameID, null, null, gameName, newChessBoard);
            gameDAO.createGame(newGame);
            createResult = new CreateResult(gameID, null);
            return createResult;
        }
    }

    public ListResult getListofGames(ListRequest listRequest) throws DataAccessException {
        String authToken = listRequest.authToken();
        if (checkAuthToken(authToken)) {
            ListResult listResult = new ListResult(null, "Error: unauthorized");
            return listResult;
        }
        GameData[] listOfGames = gameDAO.listGames();
        if (listOfGames == null) {
            ListResult listResult = new ListResult(null, null);
            return listResult;
        }
        ArrayListResult[] newList = new ArrayListResult[listOfGames.length];
        ListResult listResult = new ListResult(newList, null);
        int i = 0;
        for (GameData game : listOfGames) {
            Integer newGameID = game.gameID();
            String whiteUserName = game.whiteUsername();
            String blackUserName = game.blackUsername();
            String gameName = game.gameName();
            ArrayListResult singleGame = new ArrayListResult(newGameID, whiteUserName, blackUserName, gameName);
            listResult.games()[i] = singleGame;
            i++;
        }
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
        if (!potentialGame) {
            JoinResult joinResult = new JoinResult("Error: bad request");
            return joinResult;
        }
        if (playerColor.equals("black")) {
            if (game.blackUsername() == null) {
                gameDAO.addBlackUsername(game.gameID(), username);
                JoinResult joinResult = new JoinResult(null);
                return joinResult;
            } else {
                JoinResult joinResult = new JoinResult("Error: already taken");
                return joinResult;
            }
        } else if (playerColor.equals("white")) {
            if (game.whiteUsername() == null) {
                gameDAO.addWhiteUsername(game.gameID(), username);
                JoinResult joinResult = new JoinResult(null);
                return joinResult;
            } else {
                JoinResult joinResult = new JoinResult("Error: already taken");
                return joinResult;
            }
        }
        return new JoinResult("Error: bad request");
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
        GameData gameToCheck = gameDAO.getGameWithGameName(gameName);
        if (gameToCheck == null) {
            return false;
        } else {
            return true;
        }
    }

}
