package dataaccess;

import model.UserData;

public interface UserDAO {

    void deleteUsers() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void addUser(UserData user) throws DataAccessException;

}
