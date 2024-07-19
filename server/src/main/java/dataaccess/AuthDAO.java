package dataaccess;

import model.*;

public interface AuthDAO {
    void deleteAuthToken() throws DataAccessException;

    AuthData verifyToken(String authToken) throws DataAccessException;

    void addAuthToken(String authToken, String username) throws DataAccessException;

    String returnUserName(String authToken) throws DataAccessException;

}
