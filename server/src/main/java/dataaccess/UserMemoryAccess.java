package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemoryAccess {
    public UserMemoryAccess() {}

    final private HashMap<Integer, UserData> users = new HashMap<>();

    public void deleteUsers() {
        users.clear();
    }

}
