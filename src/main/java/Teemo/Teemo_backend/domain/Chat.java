package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;

@Entity
public class Chat {
    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    /** 전달 정보 **/
    @Column(length = 50)
    private String msg; // 메시지 (50자 제한)
    private String sender; // 송신자

    /** 매핑관계 **/
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom; // 채팅방 정보
}
