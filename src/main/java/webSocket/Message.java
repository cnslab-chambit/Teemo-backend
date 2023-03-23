package webSocket;

import lombok.Data;

@Data
public class Message {
    String sender; //송신자
    String content; //메시지 내용
    String chatRoomId; //채팅 id

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}