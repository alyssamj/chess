package dataaccess;

import model.UserData;

import java.util.HashMap;

public class DataAccess {
    private final UserMemoryAccess userMemoryAccess;
    // private final GameMemoryAccess gameMemoryAccess;
    // private final AuthMemoryAccess authMemoryAccess;
    public DataAccess(UserMemoryAccess userMemoryAccess) {
        this.userMemoryAccess = userMemoryAccess;
    }

//    UserMemoryAccess userMemoryAccess = new UserMemoryAccess();
//
//    public class UserMemoryAccess extends DataAccess implements UserDAO {
//        public UserMemoryAccess() {}
//
//        final private HashMap<String, UserData> users = new HashMap<>();
//
////        private static dataaccess.UserMemoryAccess instance;
////
////        public static dataaccess.UserMemoryAccess getInstance() {
////            if (instance == null) {
////                instance = new dataaccess.UserMemoryAccess();
////            }
////            return instance;
////        }
//        @Override
//        public UserData getUser(String username) throws DataAccessException {
//            if (users.containsKey(username)) {
//                return users.get(username);
//            }
//            return null;
//        }
//
//        public void deleteUsers() {
//            users.clear();
//        }
//
//    }
}
