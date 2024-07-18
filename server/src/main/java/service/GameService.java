package service;

import dataaccess.*;
import model.*;
import service.Requests_Responses.*;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateResult createGame(CreateRequest createRequest) {
        String authToken = createRequest.authToken();
        String gameName = createRequest.gameName();
        CreateResult createResult;
        try {
            AuthData auth = authDAO.verifyToken(authToken);
            if (auth == null) {
                createResult = new CreateResult(null, "Error: unauthorized");
                return createResult;
            }
            if (checkGame(gameName)) {
                createResult = new CreateResult(null, "Error: bad request");
                return createResult;
            } else {

            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean checkGame(String gameName) throws DataAccessException {
        GameData gameToCheck = gameDAO.getGame(gameName);
        if (gameToCheck == null) {
            return false;
        } else {
            return true;
        }
    }

}
