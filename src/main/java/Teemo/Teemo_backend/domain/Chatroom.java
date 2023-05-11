package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chatroom {
    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id; // db 탐색용 고유 식별자

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>(); // { 호스트 정보, 게스트 정보 }

    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>(); // 채팅정보

    private LocalDateTime createdAt; // 생성 시각
    private LocalDateTime deletedAt; // 만기 시각

    /** 생성자 **/
    public Chatroom(){}
    public Chatroom(
            User host,
            User guest
    ){
        users.add(host);
        users.add(guest);
        createdAt = LocalDateTime.now();
        deletedAt = createdAt.plusHours(2); // 2시간 뒤 파기
    }

}
