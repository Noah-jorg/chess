package server;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final ClearService clearService;
    private final Gson gson = new Gson();

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));


        javalin.delete("/db", ctx -> {
            clearService.clear();
            ctx.status(200).result("{}");
        });

        // Register your endpoints and exception handlers here.
        javalin.post("/user", ctx -> {
            RegisterRequest r = gson.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(r);
            ctx.status(200).result(gson.toJson(result));
        });
        javalin.post("/session", ctx -> {
            LoginRequest l = gson.fromJson(ctx.body(), LoginRequest.class);
            LoginResult result = userService.login(l);
            ctx.status(200).result(gson.toJson(result));
        });
        javalin.exception(BadRequestException.class, (ex, ctx) -> ctx.status(400).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(UnauthorizedException.class, (ex, ctx) -> ctx.status(401).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(AlreadyTakenException.class, (ex, ctx) -> ctx.status(403).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(Exception.class, (ex, ctx) -> ctx.status(500).result(gson.toJson(Map.of("message", "Error: " + ex.getMessage()))));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
