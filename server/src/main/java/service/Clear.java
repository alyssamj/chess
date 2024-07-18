package service;

import dataaccess.*;

public class Clear {



    private final UserDAO userDao;
    private final AuthDAO authDAO;

    public Clear(UserDAO userDao, AuthDAO authDAO) {
        this.userDao = userDao;
        this.authDAO = authDAO;
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

