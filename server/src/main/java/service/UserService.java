package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import io.javalin.http.BadRequestResponse;
import model.*;
import dataaccess.*;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest r) throws DataAccessException, BadRequestResponse, AlreadyTakenException {
        if (r.username() == null || r.password() == null || r.email() == null) {
            throw new BadRequestResponse("Error: bad request");
        }
        else if (userDAO.getUser(r.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        userDAO.createUser(new UserData(r.username(), r.password(), r.email()));
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, r.username()));
        return new RegisterResult(r.username(), authToken);
    }

    public LoginResult login(LoginRequest r) throws DataAccessException, BadRequestException, UnauthorizedException {
        if (r == null || r.username() == null || r.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData user = userDAO.getUser(r.username());
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        else if (!user.password().equals(r.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, r.username()));
        return new LoginResult(r.username(), authToken);
    }
}
