package api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

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
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;    // 생년월일 (만 나이를 구하기 위해서)
    @Enumerated(EnumType.STRING)
    private Role role;  // 역할, ENUM [HOST,GUEST,NOTHING]
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별, ENUM [MAN(남자),WOMAN(여자)]

    // 생성 or pick 한 tag과 매핑 (Many:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // Host 로서의 Chatroom
    @OneToMany(mappedBy = "host", cascade = CascadeType.REMOVE)
    private List<Chatroom> hostedChatrooms = new ArrayList<>();

    // Guest 로서의 Chatroom
    @OneToOne(mappedBy = "guest", cascade = CascadeType.REMOVE)
    private Chatroom guestChatroom;

    private LocalDate createAt; // 계정 생성 년월일 저장


    //==기본 생성자==//
    public Member(){}

    //==개인 정보를 담는 생성자==//
    public Member(String email,
                  String password,
                  String nickname,
                  LocalDate birthday,
                  Gender gender)
    {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.role = Role.VIEWER;
    }

    public void setTagRole(Tag tag, Role role){
        this.tag = tag;
        this.role = role;
    }

    public void setGuestChatroom(Chatroom guestChatroom) {
        this.guestChatroom = guestChatroom;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
}
