package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import service.RegisterRequest;

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

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws ResponseException {
        facade.clear();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerPositive() throws Exception {
        var result = facade.register(new RegisterRequest("noah", "password", "noah@byu.edu"));
        assertEquals("noah", result.username());
        assertTrue(result.authToken().length() > 10);
    }

    @Test
    void registerDuplicateFails() throws Exception {
        facade.register(new RegisterRequest("noah", "password", "other@byu.edu"));
        assertThrows(ResponseException.class,
                () -> facade.register(new RegisterRequest("noah", "password", "other2@gmail.com")));
    }
}
