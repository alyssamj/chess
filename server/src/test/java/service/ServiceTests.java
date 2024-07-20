package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Requests_Responses.*;

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


        JoinRequest joinRequest = new JoinRequest("authToken", "WHITE", 1);
    }


    @Test
    void clearAll() {

    }

}