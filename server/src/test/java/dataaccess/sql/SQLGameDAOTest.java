package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {
    private SQLGameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void clearGames() throws DataAccessException {
        var game = gameDAO.createGame("beginner");
        gameDAO.clear();
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        var game = gameDAO.createGame("beginner");
        assertEquals("beginner", game.gameName());
        assertEquals(new ChessGame(), game.game());
        assertEquals(game, gameDAO.getGame(game.gameID()));
    }

    @Test
    public void create2Games() throws DataAccessException {
        var game = gameDAO.createGame("beginner");
        var game2 = gameDAO.createGame("intermediate");
        assertNotEquals(game.gameID(), game2.gameID());
    }

    @Test
    public void listGameSuccess() throws DataAccessException {
        var game = gameDAO.createGame("beginner");
        var game2 = gameDAO.createGame("intermediate");
        var game3 = gameDAO.createGame("Expert");
        var gameList = gameDAO.listGames();
        assertTrue(gameList.contains(game));
        assertTrue(gameList.contains(game2));
        assertTrue(gameList.contains(game3));
    }

    @Test
    public void updateGameSuccess() throws DataAccessException {
        var game = gameDAO.createGame("beginner");
        gameDAO.updateGame(new GameData(game.gameID(), "noah",
                "zane", game.gameName(), game.game()));
        var updated = gameDAO.getGame(game.gameID());
        assertEquals("noah", updated.whiteUsername());
        assertEquals("zane", updated.blackUsername());
    }
}
