package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLUserDAOTest {

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Test
    public void createUserSuccess() throws DataAccessException {
        var data = new SQLUserDAO();
        data.clear();
        var user = new UserData("noah", "123", "123@byu.edu");
        data.createUser(user);
        assertEquals(user, data.getUser("noah"));
    }
}
