package id.ac.ui.cs.supertictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.supertictactoe.model.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    User findByPassword(String password);
    List<User> findAllByUserIdIsIn(Collection<String> userIds);
}
