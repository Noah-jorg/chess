package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
    private GameService service;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private String token;

    @BeforeEach
    public void s() throws DataAccessException {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new GameService(authDAO, gameDAO);

        token = "123";
        authDAO.createAuth(new AuthData(token, "noah"));
    }

    @Test
    public void listGamesSuccessful() throws Exception {
        gameDAO.createGame("CS 240");
        gameDAO.createGame("CS 235");
        gameDAO.createGame("CS 236");
        var result = service.listGames(token);
        assertEquals(3, result.size());
        System.out.println(result.get(0));
        System.out.println(result.get(1));
        System.out.println(result.get(2));
    }

    @Test
    public void invalidToken() {
        assertThrows(UnauthorizedException.class, () -> service.listGames("321"));
    }
}
