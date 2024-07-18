package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class AuthMemoryAccess implements AuthDAO{

    final private HashMap<String, String> authTokens = new HashMap<>();

    AuthMemoryAccess() {}

    @Override
    public void deleteAuthToken() throws DataAccessException {

    }

    @Override
    public AuthData verifyToken() throws DataAccessException {
        return null;
    }

    @Override
    public void addAuthToken(String authToken, String username) {
        authTokens.put(authToken, username);
    }

}


