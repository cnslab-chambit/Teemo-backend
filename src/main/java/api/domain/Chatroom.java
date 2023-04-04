package api.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Chatroom {
    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false, unique = true)
    private Member guest;

    public Chatroom() {
        this.createdAt = LocalDateTime.now();
    }

    public void setHost(Member host) {
        this.host = host;
    }

    public void setGuest(Member guest) {
        this.guest = guest;
    }
}
