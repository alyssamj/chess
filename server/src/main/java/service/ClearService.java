package service;

import dataaccess.*;
import RequestsandResponses.ClearResult;

public class ClearService {

    private final UserDAO userDao;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearService(UserDAO userDao, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clear() throws DataAccessException {
        boolean usersEmpty = userDao.clear();
        boolean gamesEmpty = gameDAO.clear();
        boolean authsEmpty = authDAO.clear();
        if (authsEmpty && usersEmpty && gamesEmpty) {
            ClearResult clearResult = new ClearResult(null);
            return clearResult;
        } else {
            return null;
        }
    }
}


