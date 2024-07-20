package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

    boolean clear() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void addUser(UserData user) throws DataAccessException;

    int returnUsersSize() throws DataAccessException;

}
