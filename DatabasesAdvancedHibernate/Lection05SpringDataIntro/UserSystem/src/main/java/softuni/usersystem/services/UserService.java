package softuni.usersystem.services;

import org.springframework.stereotype.Service;
import softuni.usersystem.entities.User;


public interface UserService {

    void save(User user);
}
