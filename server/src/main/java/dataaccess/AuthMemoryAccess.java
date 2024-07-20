package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class AuthMemoryAccess implements AuthDAO{

    final private HashMap<String, AuthData> authTokens = new HashMap<>();

    public AuthMemoryAccess() {
        AuthData testAuth = new AuthData("1234", "username");
        authTokens.put("1234", testAuth);
    }

    @Override
    public String returnUserName(String authToken) throws DataAccessException {
        return authTokens.get(authToken).username();
    }

    @Override
    public boolean clear() throws DataAccessException {
        authTokens.clear();
        if (authTokens.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void deleteAuthToken() throws DataAccessException {

    }

    @Override
    public AuthData verifyToken(String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)) {
            return authTokens.get(authToken);
        }
        return null;
    }

    @Override
    public void addAuthToken(String authToken, String username) {
        AuthData authData = new AuthData(authToken, username);
        authTokens.put(authToken, authData);
    }

}


