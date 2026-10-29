package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public void s() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerTest() throws Exception {
        var testResult = service.register(new RegisterRequest("noah", "c0ugarz", "nocajo01@byu.edu"));
        assertEquals("noah", testResult.username());
        assertEquals("c0guarz", testResult.authToken());
        assertNotNull(userDAO.getUser("noah"));
    }

    @Test
    public void duplicateUsername() throws Exception {
        service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        assertThrows(AlreadyTakenException.class, () -> service.register(new RegisterRequest("noah", "123", "123@gmail.com")));
    }
}
