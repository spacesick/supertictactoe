package id.ac.ui.cs.supertictactoe.service;

import java.util.List;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;

public interface UserService {

    void save(User user);

    List<User> getUserList();

    User findByUsername(String username);

    User getUserById(String userId);

    User updateUser(User existingUser, UserProfileDTO updatedProfile);

    User createUser();
}
