package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserMemoryAccess implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }

    public boolean clear() {
        users.clear();
        if (users.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
    }

    @Override
    public int returnUsersSize() throws DataAccessException {
        return users.size();
    }

}
