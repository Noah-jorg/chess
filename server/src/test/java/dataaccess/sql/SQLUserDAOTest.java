package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        var data = new SQLUserDAO();
        data.clear();
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        var data = new SQLUserDAO();
        data.clear();
        var user = new UserData("noah", "123", "123@byu.edu");
        data.createUser(user);
        assertEquals(user, data.getUser("noah"));
    }

    @Test
    public void createUserDuplicate() throws DataAccessException {
        var data = new SQLUserDAO();
        data.clear();
        data.createUser(new UserData("noah", "123", "123@byu.edu"));
        var dupe = new UserData("noah", "321", "321@byu.edu");
        var dupeDAO = new SQLUserDAO();
        assertThrows(DataAccessException.class, () -> dupeDAO.createUser(dupe));
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        var data = new SQLUserDAO();
        data.clear();
        data.createUser(new UserData("noah", "123", "123@byu.edu"));
        var result = data.getUser("noah");
        assertNotNull(result);
        assertEquals("123", result.password());
    }
}
