package dataaccess;

import model.*;

public interface AuthDAO {
    void deleteAuthToken() throws DataAccessException;

    AuthData verifyToken() throws DataAccessException;

}
