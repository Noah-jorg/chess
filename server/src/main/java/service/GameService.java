package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.List;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String token) throws UnauthorizedException, DataAccessException {
        if (token == null || authDAO.getAuth(token) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        List<GameRecord> summary = gameDAO.listGames().stream().map
                (game -> new GameRecord(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName())).toList();

        return new ListGamesResult(summary);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest r) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (authToken == null || authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (r == null || r.gameName() == null || r.gameName().isBlank()) {
            throw new BadRequestException("Error: bad request");
        }
        GameData game = gameDAO.createGame(r.gameName());
        return new CreateGameResult(game.gameID());
    }

    public void joinGame(String authToken, JoinGameRequest r) throws
            BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        if (r == null || r.playerColor() == null) {
            throw new BadRequestException("Error: bad request");
        }
        String color = r.playerColor();
        GameData game = gameDAO.getGame(r.gameID());
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            throw new BadRequestException("Error: bad request");
        }
        GameData updatedGame;
        AuthData auth = authDAO.getAuth(authToken);
        if (authToken == null || auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (color.equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        }
        else {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        }
        gameDAO.updateGame(updatedGame);
    }
}
