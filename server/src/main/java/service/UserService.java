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

    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        if (r.username() == null) {
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
}
