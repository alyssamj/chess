package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import service.Requests_Responses.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        LoginResult loginResult;

        try {
            UserData compareUser = userDAO.getUser(username);
            if (authenticateUser(compareUser, username, password)) {
                String authToken = createNewAuthToken();
                authDAO.addAuthToken(authToken, username);
                loginResult = new LoginResult(username, authToken);
                return loginResult;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        UserData newUser = new UserData(username, password, email);

        try {
            UserData checkForUser = userDAO.getUser(username);
            if (checkForUser == null) {
                userDAO.addUser(newUser);
                String authToken = createNewAuthToken();
                authDAO.addAuthToken(authToken, username);
                RegisterResult registerResult = new RegisterResult(username, authToken);
                return registerResult;
            } else {

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

    private String createNewAuthToken() {
        String authToken = UUID.randomUUID().toString();
        return authToken;
    }


}
