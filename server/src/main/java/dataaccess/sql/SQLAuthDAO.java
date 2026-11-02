package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    public AuthData getAuth(String Username) throws DataAccessException {
        throw new DataAccessException("getAuth not implemented yet");
    }
    public void createAuth(AuthData auth) throws DataAccessException {
        throw new DataAccessException("createAuth not implemented yet");
    }
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to clear auth tokens");
        }
    }
    public void deleteAuth(String authToken) {
    }
}
