package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    public AuthData getAuth(String Username) throws DataAccessException {
        throw new DataAccessException("getAuth not implemented yet");
    }
    public void createAuth(AuthData auth) throws DataAccessException {
        throw new DataAccessException("createAuth not implemented yet");
    }
    public void clear() throws DataAccessException {
        throw new DataAccessException("clear() not implemented yet");
    }
    public void deleteAuth(String authToken) {
    }
}
