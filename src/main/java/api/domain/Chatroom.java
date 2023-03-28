package api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Chatroom {
    @Id @GeneratedValue
    @Column(name="chatroom_id")
    private Long id;

    // 호스트 정보를 가지고 있는다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member guest;

    // 자신과 mapping 된 Tag 정보를 가진다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // Guest 회원 정보 설정
    public void setGuest(Member guest){
        this.guest = guest;
    }

    //==연관관계 메서드==//
    public void setTag(Tag tag){
        this.tag = tag;
        tag.addChatRoom(this);
    }


//    @OneToMany(mappedBy = "chatroom"
//            , fetch=FetchType.LAZY
//            ,cascade = CascadeType.ALL
//            , orphanRemoval = true)
//    private List<Chat> chats = new ArrayList<>();
}
