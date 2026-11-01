package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;


public class SQLUserDAO implements UserDAO {
    public UserData getUser(String username) throws DataAccessException {
        throw new DataAccessException("getUser not implemented yet");
    }

    public void createUser(UserData user) throws DataAccessException {
        throw new DataAccessException("createUser not implemented yet");
    }

    public void clear() throws DataAccessException {
        throw new DataAccessException("clear() not implemented yet");
    }
}
