package service;

import org.mindrot.jbcrypt.BCrypt;
import requestsandresponses.*;
import dataaccess.*;
import model.*;

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
        boolean authenticated = authenticateUser(compareUser, username, password);
        if (authenticated) {
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
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            RegisterResult registerResult = new RegisterResult(null, null, "Error: bad request");
            return registerResult;
        }
        String username = registerRequest.username();
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        String email = registerRequest.email();
        UserData newUser = new UserData(username, hashedPassword, email);
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
            authDAO.deleteAuthToken(authToken);
            logoutResult = new LogoutResult(null);
            return logoutResult;
        }
    }


    private boolean authenticateUser(UserData toCompare, String username, String password) {
        if (toCompare == null) {
            return false;
        } else {
            var hashedPassword = toCompare.password();
            return BCrypt.checkpw(password, hashedPassword);
        }
    }

    private String createNewAuthToken() {
        String authToken = UUID.randomUUID().toString();
        return authToken;
    }


}
