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
    private final GameService gameService;
    private final Gson gson = new Gson();

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);
        gameService = new GameService(authDAO, gameDAO);

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
        javalin.delete("/session", ctx -> {
            String token = ctx.header("authorization");
            userService.logout(token);
            ctx.status(200).result("{}");
        });
        javalin.get("/game", ctx -> {
           String token = ctx.header("authorization");
           ctx.status(200).result(gson.toJson(gameService.listGames(token)));
        });
        javalin.post("/game", ctx -> {
            String authToken = ctx.header("authorization");
            CreateGameRequest r = gson.fromJson(ctx.body(), CreateGameRequest.class);
            ctx.status(200).result(gson.toJson(gameService.createGame(authToken, r)));
        });
        javalin.put("/game", ctx -> {
            String authToken = ctx.header("authorization");
            JoinGameRequest r = gson.fromJson(ctx.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, r);
            ctx.status(200).result("{}");
        });
        javalin.exception(BadRequestException.class, (ex, ctx) -> ctx.status(400).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(UnauthorizedException.class, (ex, ctx) -> ctx.status(401).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(AlreadyTakenException.class, (ex, ctx) -> ctx.status(403).result(gson.toJson(Map.of("message", ex.getMessage()))));
        javalin.exception(Exception.class, (ex, ctx) -> ctx.status(500).result(gson.toJson(Map.of("message", "Error: " + ex.getMessage()))));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        try {
            DatabaseManager.createDatabase();
            System.out.println("Created database successfully");
        } catch (DataAccessException ex){
            throw new RuntimeException("Unable to create database");
        }
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
