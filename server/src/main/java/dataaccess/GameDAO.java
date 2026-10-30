package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
}
