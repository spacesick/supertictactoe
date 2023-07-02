package id.ac.ui.cs.supertictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.ac.ui.cs.supertictactoe.model.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, String> {
}
