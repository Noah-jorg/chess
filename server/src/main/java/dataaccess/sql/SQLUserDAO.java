package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;


public class SQLUserDAO implements UserDAO {

    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, passwordHash, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepared = conn.prepareStatement(statement)) {
                 prepared.setString(1, username);
                 try (var result = prepared.executeQuery()) {
                     if (result.next()) {
                         return new UserData(result.getString("username"), result.getString("passwordHash,"), result.getString("email"));

                     }
                     return null;
                 }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to access user data", ex);
        }
    }

    public void createUser(UserData user) throws DataAccessException {
        throw new DataAccessException("createUser not implemented yet");
    }

    public void clear() throws DataAccessException {
        try {var conn = DatabaseManager.getConnection();
            var statement = "DELETE FROM users";
            var preparedStatement = conn.prepareStatement(statement); {
                preparedStatement.executeUpdate();
            }
    } catch (SQLException ex) {
            throw new DataAccessException("Error: unable to clear users", ex);
            }
        }
    }
