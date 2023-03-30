package api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    
    // 회원 정보
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;  // 역할, ENUM [HOST,GUEST,NOTHING]
    private LocalDate birthday;    // 생년월일 (만 나이를 구하기 위해서)
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별, ENUM [MAN(남자),WOMAN(여자)]

    // 생성 or pick 한 tag과 매핑 (Many:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Chatroom> chatrooms = new ArrayList<>();

    //==기본 생성자==//
    public Member(){}

    //==개인 정보를 담는 생성자==//
    public Member(String nickname,Role role) {
        this.nickname = nickname;
        this.role = role;
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
