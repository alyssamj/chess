package service;

import requestsandresponses.*;
import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {
    UserService userService;
    GameService gameService;
    ClearService clearService;
    UserMemoryAccess userDAO = new UserMemoryAccess();
    AuthMemoryAccess authDAO = new AuthMemoryAccess();
    GameMemoryAccess gameDAO = new GameMemoryAccess();

    @BeforeEach
    void setup() throws DataAccessException {
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);
        UserData newUser = new UserData("username", "password", "email");
        userDAO.addUser(newUser);
        authDAO.addAuthToken("authToken", "username");

    }

    @Test
    void loginPreviousUser() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try {
            LoginResult loginResult = (LoginResult) userService.login(loginRequest);
            assertNotNull(loginResult.authToken());
            assertEquals(loginResult.username(), loginRequest.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest("username", "passwords");
        try {
            LoginResult loginResult = (LoginResult) userService.login(loginRequest);
            assertNotEquals(loginResult.username(), loginRequest.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createGameCorrectly() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("authToken", "gameName");

        int gameListSizeBefore = gameDAO.listGames().length;
        CreateResult createResult = gameService.createGame(createRequest);
        int gameListSizeAfter = gameDAO.listGames().length;
        System.out.println(createResult.gameID());

        assertEquals((gameListSizeBefore+1), gameListSizeAfter);
        assertNull(createResult.message());
        assertTrue(createResult.gameID() > 0);
    }

    @Test
    void createGameIncorrectAuthToken() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("auth", "gameName");
        int gameListSizeBefore = gameDAO.listGames().length;
        CreateResult createResult = gameService.createGame(createRequest);
        int gameListSizeAfter = gameDAO.listGames().length;

        assertNotEquals(gameListSizeBefore+1, gameListSizeAfter);
        assertNotNull(createResult.message());
        System.out.println(createResult.message());
    }

    @Test
    void joinGameWhite() throws DataAccessException {
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(1234, null, "user1", "gameName", game));
        JoinRequest joinRequest = new JoinRequest("authToken", "WHITE", 1234);
        JoinResult joinResult = gameService.joinGame(joinRequest);
        assertNull(joinResult.message());
        assertNotNull(gameDAO.getGame("gameName").whiteUsername());
        System.out.println(gameDAO.getGame("gameName").whiteUsername());

    }

    @Test
    void colorAlreadyTakenJoinGame() throws DataAccessException {
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(1234, "whiteUser", "user1", "gameName", game));
        JoinRequest joinRequest = new JoinRequest("authToken", "WHITE", 1234);
        JoinResult joinResult = gameService.joinGame(joinRequest);
        assertNotNull(joinResult.message());
        assertNotEquals(gameDAO.getGame("gameName").whiteUsername(), "username");
    }

    @Test
    void registerUser() throws DataAccessException {
        UserData newUser = new UserData("user", "mypassword", "gmail.com");
        RegisterRequest registerRequest = new RegisterRequest("user", "mypasswrd", "gmail.com");

        int userSizeBefore = userDAO.returnUsersSize();
        RegisterResult registerResult = userService.register(registerRequest);
        int userSizeAfter = userDAO.returnUsersSize();

        assertEquals(userSizeBefore+1, userSizeAfter);
        assertNull(registerResult.message());
        assertEquals(registerRequest.username(), registerResult.username());
        assertNotNull(registerResult.authToken());
    }

    @Test
    void missingRegistrationInfo() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("user", null, "gmail.com");
        int userSizeBefore = userDAO.returnUsersSize();;
        RegisterResult registerResult = userService.register(registerRequest);
        int userSizeAfter = userDAO.returnUsersSize();

        assertEquals(userSizeBefore, userSizeAfter);
        assertNotNull(registerResult.message());
        assertNull(registerResult.authToken());
        System.out.println(registerResult.message());
    }

    @Test
    void logoutExistinguser() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest("authToken");
        int authSizeBefore = authDAO.authTokensSize();
        LogoutResult logoutResult = userService.logout(logoutRequest);
        int authSizeAfter = authDAO.authTokensSize();

        assertEquals(authSizeBefore-1, authSizeAfter);
        assertNull(logoutResult.message());
    }

    @Test
    void logoutAuthTokenNotFound() throws DataAccessException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResult loginResult = userService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest("myAuthToken");
        int authSizeBefore = authDAO.authTokensSize();
        LogoutResult logoutResult = userService.logout(logoutRequest);
        int authSizeAfter = authDAO.authTokensSize();

        assertEquals(authSizeBefore, authSizeAfter);
        assertNotNull(logoutResult.message());
        assertEquals("Error: unauthorized", logoutResult.message());
    }

    @Test
    void listGames() throws DataAccessException {
        gameService.createGame(new CreateRequest("authToken", "myGame"));

        ListRequest listRequest = new ListRequest("authToken");
        int gameSizeBefore = gameDAO.listGames().length;
        ListResult listResult = gameService.getListofGames(listRequest);
        int gameSizeAfter = gameDAO.listGames().length;

        assertNotNull(listResult.games());
        assertNull(listResult.message());
        assertEquals(gameSizeBefore, gameSizeAfter);
    }

    @Test
    void wrongAuthTokenNoList() throws DataAccessException {
        gameService.createGame(new CreateRequest("authToken", "myGame"));

        ListRequest listRequest = new ListRequest("myAuthTOKEN");
        int gameSizeBefore = gameDAO.listGames().length;
        ListResult listResult = gameService.getListofGames(listRequest);
        int gameSizeAFter = gameDAO.listGames().length;

        assertEquals(gameSizeBefore, gameSizeAFter);
        assertNotNull(listResult.message());
        assertNull(listResult.games());
    }


    @Test
    void clearAll() throws DataAccessException {
        ClearResult clearResult = clearService.clear();
        assertEquals(0, gameDAO.listGames().length);
        assertEquals(0, userDAO.returnUsersSize());
        assertEquals(0, authDAO.authTokensSize());
        assertNull(clearResult.message());


    }

}