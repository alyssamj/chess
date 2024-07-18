package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemoryAccess implements UserDAO {
    public UserMemoryAccess() {
        users.put("alyssamj02", new UserData("alyssamj02", "andilynn", "a.gmail.com"));
    }

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }

    public void deleteUsers() {
        users.clear();
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
    }

}
