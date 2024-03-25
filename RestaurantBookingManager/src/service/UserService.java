package service;

import model.User;
import repository.UserRepo;

public class UserService {

    private UserRepo userRepo;

    public UserService() {
        this.userRepo = new UserRepo();
    }

    public User saveUser(User user) {
        userRepo.saveCustomer(user);
        return user;
    }
}
