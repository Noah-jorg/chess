package model;

import java.util.Objects;

public class UserData {
    record UserRecord(String username, String password, String email) {}
}
