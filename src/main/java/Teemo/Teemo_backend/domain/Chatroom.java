package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Chatroom {
    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id; // db 탐색용 고유 식별자

    /** 매핑관계 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private Tag tag; // 태그 정보
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member guest; // 게스트 정보
    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>(); // 채팅정보

    /** 유효시간 **/
    private LocalDateTime createdAt; // 생성 시각
    private LocalDateTime deletedAt; // 만기 시각

    /** 생성자 **/
    public Chatroom(){}
    private Chatroom(
            Member guest,
            Tag tag,
            LocalDateTime createdAt,
            LocalDateTime deletedAt
    ){
        this.guest = guest;
        this.tag = tag;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    /** Chatroom 생성 method **/
    public static Chatroom createChatroom(
            Member guest,
            Tag tag
    ){
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime deletedAt = createdAt.plusHours(2);
        Chatroom chatroom = new Chatroom(guest,tag,createdAt,deletedAt);
        guest.setChatroom(chatroom);
        tag.addChatroom(chatroom);
        return chatroom;
    }

    public void removeGuest(){
        this.guest.unsetChatroom();
        this.guest = null;
    }
    public void removeTag(){
        this.tag.removeChatroom();
        this.tag = null;
    }
}
