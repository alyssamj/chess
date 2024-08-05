package client;

import dataaccess.*;
import org.junit.jupiter.api.*;
import requestsandresponses.*;
import server.Server;
import server.ServerFacade;
import ui.PreloginREPL;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
    LoginRequest loginRequest = new LoginRequest("username", "password");


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO= new MySQLUserAccess();
        authDAO = new MySQLAuthAccess();
        gameDAO = new MySQLGameAccess();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    void register() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        var authData = facade.register(registerRequest);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void unableToRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("u", "p", "e");
        var authData = facade.register(registerRequest);
        RegisterRequest registerRequest2 = new RegisterRequest("u", "p", "e");

        assertNull(facade.register(registerRequest2));
    }

    @Test
    void login() throws Exception {
      //  RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        var authData = facade.register(registerRequest);
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        var loggedOut = facade.logout(logoutRequest);
        var loggedIn = facade.login(loginRequest);
        assertNull(loggedIn.message());
    }

    @Test
    void userDoesntExist() throws Exception {
        int sizeBefore = authDAO.authTokensSize();
        var loggedIn = facade.login(loginRequest);
        int sizeAfter = authDAO.authTokensSize();
        assertNull(loggedIn);
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void logout() throws Exception {
        var authData = facade.register(registerRequest);
        int sizeBefore = authDAO.authTokensSize();
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        var loggedOut = facade.logout(logoutRequest);
        int sizeAfter = authDAO.authTokensSize();

        assertNull(loggedOut.message());
        assertEquals(sizeBefore-1, sizeAfter);
    }

    @Test
    void wrongAuthTokenForLogout() throws Exception {
        var authData = facade.register(registerRequest);
        int sizeBefore = authDAO.authTokensSize();
        String fakeAuthToken = "abcdefghijklmnop";
        LogoutRequest logoutRequest = new LogoutRequest(fakeAuthToken);
        var loggedOut = facade.logout(logoutRequest);
        int sizeAfter = authDAO.authTokensSize();

        assertNull(loggedOut);
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void createGame() throws Exception {
        var authData = facade.register(registerRequest);
        int sizeBefore = gameDAO.listGames().length;
        CreateRequest createRequest = new CreateRequest(authData.authToken(), "game");
        var creation = facade.createGame(createRequest);
        int sizeAfter = gameDAO.listGames().length;

        assertEquals(1, creation.gameID());
        assertNull(creation.message());
        assertEquals(sizeBefore+1, sizeAfter);
    }

    @Test
    void nullGameName() throws Exception {
        var authData = facade.register(registerRequest);
        int sizeBefore = gameDAO.listGames().length;
        CreateRequest createRequest = new CreateRequest(authData.authToken(), null);
        var creation = facade.createGame(createRequest);
        int sizeAfter = gameDAO.listGames().length;
        assertNull(creation);
        assertEquals(sizeBefore, sizeAfter);
    }


    @Test
    void joinGame() throws Exception {
        var authData = facade.register(registerRequest);
        int sizeBefore = gameDAO.listGames().length;
        CreateRequest createRequest = new CreateRequest(authData.authToken(), "game");
        var creation = facade.createGame(createRequest);
        int sizeAfter = gameDAO.listGames().length;
        JoinRequest joinRequest = new JoinRequest(authData.authToken(), registerRequest.username(), creation.gameID());
        var joiningGame = facade.joinGame(joinRequest);

        assertEquals(joiningGame.getClass(), JoinResult.class);
        //assertNull(joiningGame.message());


    }




    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
