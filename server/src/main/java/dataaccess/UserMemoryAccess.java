package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemoryAccess implements UserDAO {
    public UserMemoryAccess() {
    }

    final private HashMap<String, UserData> users = new HashMap<>();

//    private static UserMemoryAccess instance;
//
//    public static UserMemoryAccess getInstance() {
//        if (instance == null) {
//            instance = new UserMemoryAccess();
//        }
//        return instance;
//    }
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

}
