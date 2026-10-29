package dataaccess;
import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> tokens = new HashMap<>();

    public void createAuth(AuthData token) {
        tokens.put(token.authToken(), token);
    }

    public AuthData getAuth(String authToken) {
        return tokens.get(authToken);
    }

    public void clear() {
        tokens.clear();
    }

    public void deleteAuth(String authToken) {
        tokens.remove(authToken);
    }
}
