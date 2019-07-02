package softuni.usersystem.services.impl;

import org.springframework.stereotype.Service;
import softuni.usersystem.entities.User;
import softuni.usersystem.repositories.UserRepository;
import softuni.usersystem.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void save(User user) {
        this.userRepository.saveAndFlush(user);
    }
}
