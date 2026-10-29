package dataaccess;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> users = new HashMap<>();

    public void createUser(UserData u) {
        users.put(u.username(), u);
    }

    public UserData getUser(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            return null;
        }
    }

    public void clear() {
        users.clear();
    }
}
