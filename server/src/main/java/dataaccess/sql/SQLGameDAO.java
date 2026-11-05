package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    private static final Gson GSON = new Gson();

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error: uable to clear games", ex);
        }
    };

    public GameData createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        var game = new ChessGame();
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, GSON.toJson(game));
            preparedStatement.executeUpdate();
            try (var keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new GameData(keys.getInt(1), null, null, gameName, game);
                }
                throw new DataAccessException("Error: no game ID");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: could not create new game");
        }
    };

    public Collection<GameData> listGames() throws DataAccessException {
        var gameList = new ArrayList<GameData>();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement);
             var result = preparedStatement.executeQuery()) {
            while (result.next()) {
                GameData newGame = new GameData(result.getInt("gameID"), result.getString("whiteUsername"),
                        result.getString("blackUsername"), result.getString("gameName"),
                        GSON.fromJson(result.getString("game"), ChessGame.class));
                gameList.add(newGame);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: cannot list games", ex);
        }
        return gameList;
    };
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement
                     ("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?")) {
             preparedStatement.setInt(1, gameID);
             try (var result = preparedStatement.executeQuery()) {
                 if (result.next()) {
                     return new GameData(result.getInt("gameID"), result.getString("whiteUsername"),
                             result.getString("blackUsername"), result.getString("gameName"),
                             GSON.fromJson(result.getString("game"), ChessGame.class));
                 } else {
                     return null;
                 }
             }
        } catch (SQLException ex) {
                 throw new DataAccessException("Error: unable to read game", ex);
        }
    };

    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, GSON.toJson(game.game()));
            preparedStatement.setInt(5, game.gameID());
            if (preparedStatement.executeUpdate() == 0) {
                throw new DataAccessException("Error: could not update the game");
            }
        } catch (SQLException ex) {
                throw new DataAccessException("Error, could not update game", ex);
        }
    };
}
