package dataaccess;

import model.UserData;

public interface UserDAO {

    boolean clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void addUser(UserData user) throws DataAccessException;

}
