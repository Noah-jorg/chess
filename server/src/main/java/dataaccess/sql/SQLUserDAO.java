package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;


public class SQLUserDAO implements UserDAO {
    public UserData getUser(String username) throws DataAccessException {
        throw new DataAccessException("getUser not implemented yet");
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
