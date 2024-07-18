package service;

import dataaccess.*;
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

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        String username = loginRequest.username();
        String password = loginRequest.password();
        LoginResult loginResult;

        UserData compareUser = userDAO.getUser(username);
        if (authenticateUser(compareUser, username, password)) {
            String authToken = createNewAuthToken();
            authDAO.addAuthToken(authToken, username);
            loginResult = new LoginResult(username, authToken, null);
            return loginResult;
        } else {
            loginResult = new LoginResult(null, null, "Error: unauthorized");
            return loginResult;
        }
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        UserData newUser = new UserData(username, password, email);
        UserData checkForUser = userDAO.getUser(username);
        if (checkForUser == null) {
            userDAO.addUser(newUser);
            String authToken = createNewAuthToken();
            authDAO.addAuthToken(authToken, username);
            RegisterResult registerResult = new RegisterResult(username, authToken, null);
            return registerResult;
        } else {
            RegisterResult registerResult = new RegisterResult(null, null, "Error: already taken");
            return registerResult;
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        String authToken = logoutRequest.authToken();
        LogoutResult logoutResult;
        AuthData auth = authDAO.verifyToken(authToken);
        if (auth == null) {
            logoutResult = new LogoutResult("Error: unauthorized");
            return logoutResult;
        } else {
            logoutResult = new LogoutResult(null);
            return logoutResult;
        }
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
