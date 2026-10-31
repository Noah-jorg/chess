package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(3, result.games().size());
        System.out.println(result.games().get(0).gameName());
        System.out.println(result.games().get(1).gameName());
        System.out.println(result.games().get(2).gameName());
    }

    @Test
    public void invalidToken() {
        assertThrows(UnauthorizedException.class, () -> service.listGames("321"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        var result = service.createGame(token, new CreateGameRequest("First"));
        var gameList = service.listGames(token);
        assertEquals(1, gameList.games().size());
        assertEquals("First", gameList.games().getFirst().gameName());
    }

    @Test
    public void invalidCreateGame() throws Exception {
        assertThrows(UnauthorizedException.class, () -> service.createGame("321", new CreateGameRequest("Second")));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        var game = gameDAO.createGame("first");
        service.joinGame(token, new JoinGameRequest("WHITE", game.gameID()));
        assertEquals("noah", gameDAO.getGame(game.gameID()).whiteUsername());
        assertNull(gameDAO.getGame(game.gameID()).blackUsername());
    }

    @Test
    public void joinGameAlreadyTaken() throws Exception {
        var game = gameDAO.createGame("first");
        authDAO.createAuth(new AuthData("321", "Zane"));
        service.joinGame(token, new JoinGameRequest("WHITE", game.gameID()));
        assertThrows(AlreadyTakenException.class, () ->
                service.joinGame("321", new JoinGameRequest("WHITE", game.gameID())));
    }

    @Test
    public void gameNotFound() throws Exception {
        assertThrows(BadRequestException.class, () ->
                service.joinGame(token, new JoinGameRequest("WHITE", 42)));
    }
}
