package api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    private int limit;

    // Chatroom 을 관리 (1:Many)
    // Tag 가 사라지면 관련 Chatroom 도 싹 정리
    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> chatrooms = new ArrayList<>();

    //==기본 생성자==//
    public Tag() {}

    //==(위치 + 모집) 정보를 받을 생성자==//
    public Tag(int limit) {
        this.limit = limit;
    }

    //==연관관계 메서드==//
    public void setHost(Member host) {
        this.host = host;
        host.setTag(this);
    }

    //==
    public void addChatRoom(Chatroom chatroom){
        /**
        * 추후에 limit 이용해서 채팅방 객수 조절

         */

        this.chatrooms.add(chatroom);
    }
}

