package id.ac.ui.cs.friendservice.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "friend_request")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    public FriendRequest(String senderId, String receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FriendRequest that = (FriendRequest) o;
        return requestId != null && Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
