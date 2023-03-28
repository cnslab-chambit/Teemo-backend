package api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    
    // 회원 정보
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;  //ENUM [HOST,GUEST,NOTHING]
    
    
    // 생성 or pick 한 tag과 매핑 (Many:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //==기본 생성자==//
    public Member(){}

    //==개인 정보를 담는 생성자==//
    public Member(String nickname,Role role) {
        this.nickname = nickname;
        this.role = role;
    }

    public void setTag(Tag tag){
        this. tag = tag;
    }
}
