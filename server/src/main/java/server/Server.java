package server;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import service.AlreadyTakenException;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final Gson gson = new Gson();

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        userService = new UserService(userDAO, authDAO);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", ctx -> {
            RegisterRequest r = gson.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(r);
            ctx.status(200).result(gson.toJson(result));
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
