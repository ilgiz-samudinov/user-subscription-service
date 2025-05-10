package example.com.usersubscriptionservice.application.port.in;

import example.com.usersubscriptionservice.domain.model.User;

import java.util.List;

public interface UserUseCase {
    User createUser(User user);
    List<User> getAllUsers();
    User updateUser(Long id,  User user);
    void deleteUser(Long id);
    User getUser(Long id);
}
