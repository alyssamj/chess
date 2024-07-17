package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemoryAccess {
    public UserMemoryAccess() {}

    final private HashMap<Integer, UserData> users = new HashMap<>();

    private static UserMemoryAccess instance;

    public static UserMemoryAccess getInstance() {
        if (instance == null) {
            instance = new UserMemoryAccess();
        }
        return instance;
    }

    public void deleteUsers() {
        users.clear();
    }

}
