package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserMemoryAccess;

public class Clear {



    private final UserDAO userDAO;

    public Clear(UserDAO userDAO) {
        this.userDAO = userDAO;
        }
    }
//
//    public void clear() {
//        try {
//            userDAO.deleteUsers();
//        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
