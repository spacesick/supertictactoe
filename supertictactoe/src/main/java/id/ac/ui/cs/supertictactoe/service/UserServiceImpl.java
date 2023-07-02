package id.ac.ui.cs.supertictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.UserAuthority;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;
import id.ac.ui.cs.supertictactoe.repository.UserAuthorityRepository;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public final User getUserById(final String userId) {
        return userRepository.getReferenceById(userId);
    }

    @Override
    public final User updateUser(final User existingUser, final UserProfileDTO updateProfile) {
        existingUser.setFirstName(updateProfile.getFirstName());
        existingUser.setLastName(updateProfile.getLastName());
        return userRepository.save(existingUser);
    }

    @Override
    public final User createUser() {
        return new User();
    }

    @Override
    public final void save(final User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        var grantedAuthority = new UserAuthority();
        grantedAuthority.setAuthority("USER");
        grantedAuthority.setUserId(user);
        user.setAuthorities(Collections.singleton(grantedAuthority));
        userRepository.save(user);
        userAuthorityRepository.save(grantedAuthority);
    }

    @Override
    public final List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public final User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

}
