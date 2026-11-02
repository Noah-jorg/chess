package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLAuthDAOTest {
    private SQLUserDAO userDAO;
    private SQLAuthDAO authDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        userDAO.clear();
        authDAO.clear();
        userDAO.createUser(new UserData("noah", "123", "123@byu.edu"));
    }

    @Test
    public void createAuthSuccessful() throws DataAccessException {
        var auth = new AuthData("First-token", "noah");
        authDAO.createAuth(auth);
        assertEquals(auth, authDAO.getAuth("First-token"));
    }
}
