package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

import java.util.List;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public List<GameRecord> listGames(String token) throws UnauthorizedException, DataAccessException {
        if (token == null || authDAO.getAuth(token) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames().stream().map(game -> new GameRecord(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName())).toList();
    }
}
