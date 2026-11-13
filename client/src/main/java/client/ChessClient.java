package client;

import exception.ResponseException;
import service.LoginRequest;
import service.RegisterRequest;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private static ServerFacade server = null;
    private static State state = State.LOGGED_OUT;

    public ChessClient(int port) throws ResponseException {
        server = new ServerFacade(port);
        run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e ) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1 , tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "register" -> register(params);
                case "login" -> login(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        var request = new RegisterRequest(params[0], params[1], params[2]);
        server.register(request);
        return String.format("You registered as %s", params[0]);
    }

    public String login(String... params) throws ResponseException {
        var request = new LoginRequest(params[0], params[1]);
        server.login(request);
        state = State.LOGGED_IN;
        return String.format("You signed in as %s", params[0]);
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                       register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                       login <USERNAME> <PASSWORD> - to play chess
                       quit - to quit
                       help - with possible commands
                    """;
        }
        return """
                   create <NAME> - a game
                   list - games
                   join <ID> [WHITE|BLACK] - a game
                   observe <ID> - a game
                   logout - when you are done
                   quit - playing chess
                   help - with possible commands
                """;
    }

    private void printPrompt() {
        if (state == State.LOGGED_OUT){
            System.out.print("[LOGGED_OUT] >>> ");
        }
        else {
            System.out.print("[LOGGED_IN] >>> ");
        }
    }
}
