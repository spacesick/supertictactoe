package id.ac.ui.cs.gameservice.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "game_id")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ElementCollection(targetClass = Mark.class)
    @Enumerated(EnumType.ORDINAL)
    List<Mark> marks;

    int size;

    int winLength;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    List<Player> players;

    @Column(name = "current_player_turn")
    int currentPlayerTurnIndex;

    @Column(name = "winner")
    @Builder.Default
    int winnerIndex = -1;

    @Column(name = "is_over")
    boolean isOver;
}
