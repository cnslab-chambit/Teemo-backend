package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Chat {
    @Id
//    @GeneratedValue // timestamp 를 아이디로 사용.
    @Column(name = "chat_id")
    private Long id;

    /** 전달 정보 **/
    @Column(length = 50)
    private String msg; // 메시지 (50자 제한)
    private String sender; // 송신자

    /** 매핑관계 **/
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom; // 채팅방 정보

    /** 생성자 **/
    public Chat(){}
    private Chat(Long id, String msg, String sender){
        this.id = id;
        this.msg = msg;
        this.sender = sender;
    }
    /** Chat 생성 method **/
    public static Chat createChat(Long id, String msg, String sender,Chatroom chatroom){
        Chat chat = new Chat(id,msg,sender);
        chat.setChatroom(chatroom);
        return chat;
    }

    public void setChatroom(Chatroom chatroom){
        this.chatroom = chatroom;
        chatroom.addChats(this);
    }
}
