package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    public void clear() throws DataAccessException {
        throw new DataAccessException("clear not implemented yet");
    };
    public GameData createGame(String gameName) throws DataAccessException {
        throw new DataAccessException("createGame not implemented yet");
    };
    public Collection<GameData> listGames() throws DataAccessException {
        throw new DataAccessException("listGames not implemented yet");
    };
    public GameData getGame(int gameID) throws DataAccessException {
        throw new DataAccessException("getGame not implemented yet");
    };
    public void updateGame(GameData game) throws DataAccessException {
        throw new DataAccessException("updateGame not implemented yet");
    };
}
