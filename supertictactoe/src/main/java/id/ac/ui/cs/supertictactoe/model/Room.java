package id.ac.ui.cs.supertictactoe.model;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {

    @Id
    @Column(name = "room_id")
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy= "org.hibernate.id.UUIDGenerator")
    private String roomId;

    @NotEmpty(message = "Room name cannot be empty.")
    @Column(name = "room_name")
    private String roomName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "player1_id", referencedColumnName = "user_id")
    private User player1;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "player2_id", referencedColumnName = "user_id")
    private User player2;

    @Enumerated(EnumType.ORDINAL)
    private RoomStatus status;

    public Room() {
        this.status = RoomStatus.EMPTY;
    }

    public Room(String roomId, String roomName, User player1, User player2) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.player1 = player1;
        this.player2 = player2;
        this.status = RoomStatus.EMPTY;
    }
}
