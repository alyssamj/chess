package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import service.Requests_Responses.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        try {
            UserData compareUser = userDAO.getUser(username);
            if (authenticateUser(compareUser, username, password)) {
                UUID.randomUUID().toString();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }




        return null;
    }

    private boolean authenticateUser(UserData toCompare, String username, String password) {
        if (Objects.equals(toCompare.username(), username) && Objects.equals(toCompare.password(), password)) {
            return true;
        } else {
            return false;
        }
    }


}
