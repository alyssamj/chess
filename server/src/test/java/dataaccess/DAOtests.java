package dataaccess;

import org.mindrot.jbcrypt.BCrypt;
import requestsandresponses.*;
import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAOtests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    GameData newGame = new GameData(1234, null, null, "Test Game", new ChessGame());
    UserData testUser = new UserData("username", "password", "email");

    @BeforeEach
    void setUp() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            gameDAO = new MySQLGameAccess();
            userDAO = new MySQLUserAccess();
            authDAO = new MySQLAuthAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearAll() throws DataAccessException {
        gameDAO.createGame(newGame);
        userDAO.addUser(testUser);
        authDAO.addAuthToken("authToken", "username");

        int gameSizeBefore = gameDAO.listGames().length;
        int userSizeBefore = userDAO.returnUsersSize();
        int authSizeBefore = authDAO.authTokensSize();

        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();

        int gameSizeAfter = gameDAO.listGames().length;
        int userSizeAfter = userDAO.returnUsersSize();
        int authSizeAfter = authDAO.authTokensSize();

        assertNotEquals(gameSizeAfter, gameSizeBefore);
        assertNotEquals(userSizeAfter, userSizeBefore);
        assertNotEquals(authSizeAfter, authSizeBefore);

        assertEquals(0, gameSizeAfter);
        assertEquals(0, userSizeAfter);
        assertEquals(0, authSizeAfter);
    }

    @Test
    void getUserPass() throws DataAccessException {
        userDAO.clear();
        userDAO.addUser(testUser);

        UserData myUser = userDAO.getUser("username");
        assertEquals(testUser, myUser);
    }

    @Test
    void getUserFailure() throws DataAccessException {
        userDAO.clear();
        assertNull(userDAO.getUser("username"));

    }

    @Test
    void addUserPass() throws DataAccessException{
        userDAO.clear();
        int userSizeBefore = userDAO.returnUsersSize();
        userDAO.addUser(testUser);
        int userSizeAfter = userDAO.returnUsersSize();

        assertEquals(userSizeBefore+1, userSizeAfter);
        assertEquals(userDAO.getUser("username"), testUser);
    }

    @Test
    void addUserFailBecauseOfNullValues() throws DataAccessException {
        userDAO.clear();
        assertThrows(DataAccessException.class, () -> {
            userDAO.addUser(new UserData(null, null, null));
        });
    }

    @Test
    void correctlyReturnUsersSize() throws DataAccessException{
        userDAO.clear();
        int currentSize = userDAO.returnUsersSize();
        assertEquals(0, currentSize);

        userDAO.addUser(testUser);
        int newSize = userDAO.returnUsersSize();

        assertEquals(1, newSize);
    }

    @Test
    void incorrectlyReturnUsersSize() throws DataAccessException {
        assertNotNull(userDAO.returnUsersSize());
        int userSize = userDAO.returnUsersSize();
        userDAO.clear();
        assertNotEquals(userSize, userDAO.returnUsersSize());

    }
}