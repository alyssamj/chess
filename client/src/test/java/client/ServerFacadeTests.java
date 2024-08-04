package client;

import org.junit.jupiter.api.*;
import requestsandresponses.RegisterRequest;
import server.Server;
import server.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

//    @BeforeEach
//    public static void setUp() {
//        facade.;
//    }

    @Test
    void register() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        var authData = facade.register(registerRequest);
        assertTrue(authData.authToken().length() > 10);
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
