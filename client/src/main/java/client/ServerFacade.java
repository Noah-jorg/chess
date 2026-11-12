package client;

import service.*;

import java.net.http.HttpClient;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) {
    }

    public LoginResult login(LoginRequest request) {

    }

    public void joinGame(JoinGameRequest request) {

    }



}
