package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int ID = 1;

    public void clear() {
        games.clear();
        ID = 1;
    }

    public GameData createGame(String name) {
        GameData game = new GameData(ID++, null, null, name, new ChessGame());
        games.put(game.gameID(), game);
        return game;
    }

    public Collection<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Error: game doesn't exist");
        }
        games.put(game.gameID(), game);
    }
}
