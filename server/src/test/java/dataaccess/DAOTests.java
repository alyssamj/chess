package dataaccess;

import chess.ChessGame;
import model.AuthData;
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
    AuthData authToken = new AuthData("authToken", "username");

    @BeforeEach
    void setUp() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            gameDAO = new MySQLGameAccess();
            userDAO = new MySQLUserAccess();
            authDAO = new MySQLAuthAccess();
            gameDAO.clear();
            userDAO.clear();
            authDAO.clear();
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
    void clearUsers() throws DataAccessException {
        userDAO.clear();
        assertEquals(0, userDAO.returnUsersSize());
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
        userDAO.addUser(testUser);
        int userSize = userDAO.returnUsersSize();
        userDAO.clear();
        assertNotEquals(userSize, userDAO.returnUsersSize());
    }

    @Test
    void listGames() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);

        GameData[] myGames = gameDAO.listGames();
        assertEquals(1, myGames.length);
        assertEquals(myGames[0].gameID(), 1234);
        assertNotNull(myGames);
    }

    @Test
    void noGamesToList() throws DataAccessException {
        gameDAO.clear();

        GameData[] myGames = gameDAO.listGames();
        assertEquals(0, myGames.length);
    }

    @Test
    void clearGames() throws DataAccessException {
        gameDAO.clear();

        assertEquals(0, gameDAO.listGames().length);
    }

    @Test
    void getGameWithGameNamePass() throws DataAccessException{
        gameDAO.createGame(newGame);
        String gameName = "Test Game";
        GameData myGame = gameDAO.getGameWithGameName(gameName);
        assertEquals(1234, myGame.gameID());
        assertEquals(null, myGame.whiteUsername());

    }

    @Test
    void failToGetGame() throws DataAccessException {
        gameDAO.clear();
        assertNull(gameDAO.getGameWithGameName("myGame"));
    }

    @Test
    void createGame() throws DataAccessException {
        gameDAO.clear();
        int sizeBefore = gameDAO.listGames().length;
        gameDAO.createGame(newGame);
        int sizeAfter= gameDAO.listGames().length;
        GameData game = gameDAO.getGameWithID(1234);

        assertEquals(sizeBefore+1, sizeAfter);
        assertEquals(1234, game.gameID());
        assertEquals(1234, newGame.gameID());

    }

    @Test
    void duplicateGameName() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(newGame);
        });
    }

    @Test
    void getGameWithID() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);

        GameData testGame = gameDAO.getGameWithID(1234);
        assertEquals(1234, testGame.gameID());
        assertNotNull(testGame);
    }

    @Test
    void getGameIDisNull() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGameWithID(null);
        });
    }

    @Test
    void addBlackUsername() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        String blackUsername = "black username";
        gameDAO.addBlackUsername(1234, blackUsername);

        assertEquals(blackUsername, gameDAO.getGameWithID(1234).blackUsername());
    }

    @Test
    void blackUsernameAlready() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        String blackUsername = "black username";
        gameDAO.addBlackUsername(1234, blackUsername);
        assertThrows(DataAccessException.class, () -> {
            gameDAO.addBlackUsername(1234, "myusername");
        });

    }

    @Test
    void addWhiteUsername() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        String whiteUsername = "white username";
        gameDAO.addWhiteUsername(1234, whiteUsername);

        assertEquals("white username", gameDAO.getGameWithID(1234).whiteUsername());
    }

    @Test
    void whiteUsernameAlreadyTaken() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame(newGame);
        String whiteUsername = "white user";
        gameDAO.addWhiteUsername(1234, whiteUsername);
        assertThrows(DataAccessException.class, () -> {
           gameDAO.addWhiteUsername(1234, "usernameForWhite");
        });

    }
    @Test
    void deleteAuthToken() throws DataAccessException {
        authDAO.clear();
        authDAO.addAuthToken(authToken.authToken(), authToken.username());
        int authSizeBefore = authDAO.authTokensSize();
        authDAO.deleteAuthToken(authToken.authToken());
        int authSizeAfter = authDAO.authTokensSize();

        assertEquals(authSizeBefore-1, authSizeAfter);
    }

    @Test
    void deleteAuthTokenNotFound() throws DataAccessException {
        authDAO.clear();
        assertThrows(DataAccessException.class, () -> {
           authDAO.deleteAuthToken(authToken.authToken());
        });
    }

    @Test
    void verifyToken() throws DataAccessException {
        authDAO.clear();
        authDAO.addAuthToken(authToken.authToken(), authToken.username());
        AuthData testAuthToken = authDAO.verifyToken(authToken.authToken());

        assertEquals(authToken, testAuthToken);
    }

    @Test
    void incorrectToken() throws DataAccessException {
        authDAO.clear();
        authDAO.addAuthToken(authToken.authToken(), authToken.username());

        AuthData testAuthToken = authDAO.verifyToken("auth");
        assertNull(testAuthToken);
    }

    @Test
    void addAuthToken() throws DataAccessException {
       authDAO.clear();
       int authSizeBefore = authDAO.authTokensSize();
       authDAO.addAuthToken(authToken.authToken(), authToken.username());
       int authSizeAfter = authDAO.authTokensSize();
       assertEquals(authSizeBefore+1, authSizeAfter);
       assertEquals(authToken, authDAO.verifyToken(authToken.authToken()));
    }

    @Test
    void nullAuthandUsername() throws DataAccessException {
        authDAO.clear();
        assertThrows(DataAccessException.class, () -> {
            authDAO.addAuthToken(null, null);
        });
    }

    @Test
    void returnUserName() throws DataAccessException {
        authDAO.clear();
        authDAO.addAuthToken(authToken.authToken(), authToken.username());
        String username = authDAO.returnUserName(authToken.authToken());
        assertEquals(authToken.username(), username);
    }

    @Test
    void noAuthTokenInDatabase() throws DataAccessException {
        authDAO.clear();
        String username = authDAO.returnUserName(authToken.authToken());
        assertNull(username);
    }

    @Test
    void clearAuth() throws DataAccessException{
        authDAO.clear();
        assertEquals(0, authDAO.authTokensSize());
    }

    @Test
    void authTokensSizeCorrect() throws DataAccessException {
        authDAO.clear();
        assertEquals(0, authDAO.authTokensSize());
    }

    @Test
    void authTokensSizeIncorrect() throws DataAccessException {
        authDAO.clear();
        authDAO.addAuthToken(authToken.authToken(), authToken.username());
        assertNotEquals(0, authDAO.authTokensSize());
    }

 }