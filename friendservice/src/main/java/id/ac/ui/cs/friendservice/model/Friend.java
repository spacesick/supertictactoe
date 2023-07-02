package id.ac.ui.cs.friendservice.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "friend")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Column(name = "user_1_id", nullable = false)
    private String user1Id;

    @Column(name = "user_2_id", nullable = false)
    private String user2Id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Friend that = (Friend) o;
        return friendId != null && Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
