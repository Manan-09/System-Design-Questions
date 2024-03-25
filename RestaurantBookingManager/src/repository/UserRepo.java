package repository;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {

    private Map<String, User> userById = new HashMap<>();

    public void saveCustomer(User user) {
        userById.put(user.getId(), user);
    }

}
