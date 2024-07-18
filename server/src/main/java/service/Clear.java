package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserMemoryAccess;

public class Clear {



    private final DataAccess dataAccess;

    public Clear(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
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

