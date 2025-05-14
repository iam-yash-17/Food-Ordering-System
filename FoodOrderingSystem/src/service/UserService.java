package service;

import model.User ;
import java.util.* ;

public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public String createUser(String name) {
        User newUser = new User(name);
        users.put(newUser.getId(), newUser);
        return newUser.getId();
    }

    public User getUserById(String id) {
        return users.get(id);
    }
}
