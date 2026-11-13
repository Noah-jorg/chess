package client;

import exception.ResponseException;
import service.LoginRequest;
import service.RegisterRequest;

import java.util.Scanner;

public class ChessClient {
    private static ServerFacade server = null;
    private static State state = State.LOGGED_OUT;

    public ChessClient(int port) throws ResponseException {
        server = new ServerFacade(port);
    }

    public void run() {
        prelogin();

    }

    private static void prelogin() {
        Scanner scanner = new Scanner(System.in);
        while (state == State.LOGGED_OUT) {
            System.out.print("[LOGGED OUT] >>> ");

            if (!scanner.hasNextLine()) {
                System.out.print("Please input a valid command. Type \"help\" for commands.");
                continue;
            }
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                break;
            } else if (input.equalsIgnoreCase("help")) {
                System.out.print("   register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                System.out.print("   login < USERNAME> <PASSWORD> - to play chess");
                System.out.print("   quit - playing chess");
                System.out.print("   help - with possible commands");
            } else if (input.equalsIgnoreCase("register")) {
                var list = input.split(" ");
                try {
                    var request = new RegisterRequest(list[1], list[2], list[3]);
                    server.register(request);
                } catch (NullPointerException | ResponseException ex) {
                    System.err.println("Error: Unexpected amount of inputs.");
                }
            } else if (input.equalsIgnoreCase("login")) {
                var list = input.split(" ");
                try {
                    var request = new LoginRequest(list[1], list[2]);
                    server.login(request);
                    state = State.LOGGED_IN;
                } catch (NullPointerException | ResponseException ex) {
                    System.err.println("Error: check credentials or unable to login");
                }
            }
        }
    }
}
