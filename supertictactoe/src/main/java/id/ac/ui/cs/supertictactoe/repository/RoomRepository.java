package id.ac.ui.cs.supertictactoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.supertictactoe.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

}
