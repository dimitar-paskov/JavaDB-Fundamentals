package softuni.usersystem.services;

import org.springframework.stereotype.Service;
import softuni.usersystem.entities.User;


public interface UserService {

    long getUsersCount();
    void save(User user);
}
