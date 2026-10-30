package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void s() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerTest() throws Exception {
        var testResult = service.register(new RegisterRequest("noah", "c0ugarz", "nocajo01@byu.edu"));
        assertEquals("noah", testResult.username());
        assertNotNull(testResult.authToken());
        assertNotNull(userDAO.getUser("noah"));

        // check if token handed back is token stored in db by testing username saved to token
        AuthData actual = authDAO.getAuth(testResult.authToken());
        assertEquals("noah", actual.username());
    }

    @Test
    public void duplicateUsername() throws Exception {
        service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        assertThrows(AlreadyTakenException.class, () -> service.register(new RegisterRequest("noah", "123", "123@gmail.com")));
    }

    @Test
    public void loginSuccess() throws Exception {
        var register = service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        var loginResult = service.login(new LoginRequest("noah", "123"));
        assertEquals("noah", loginResult.username());
        assertNotNull(loginResult.authToken());
        assertNotNull(authDAO.getAuth(loginResult.authToken()));
    }

    @Test
    public void loginFailure() throws Exception {
        service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest("noah", "1234")));
    }

    @Test
    public void loginNoPasswordOrUsername() throws Exception {
        service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        assertThrows(BadRequestException.class, () -> service.login(new LoginRequest("noah", null)));
        assertThrows(BadRequestException.class, () -> service.login(new LoginRequest(null, "123")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        var register = service.register(new RegisterRequest("noah", "123", "123@gmail.com"));
        service.logout(register.authToken());
        assertNull(authDAO.getAuth(register.authToken()));
    }

    @Test
    public void invalidLogout() {
        assertThrows(UnauthorizedException.class, () -> service.logout("gabagool"));
    }
}
