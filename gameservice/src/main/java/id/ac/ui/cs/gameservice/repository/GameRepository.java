package id.ac.ui.cs.gameservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.gameservice.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {

    List<Game> findByPlayersUsername(String username);
}
