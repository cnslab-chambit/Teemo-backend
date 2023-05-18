package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    /** 가입 정보 **/
    private String email; // 이메일 주소
    private String password; // 비밀번호
    @Column(length = 10)
    private String nickname; // 닉네임
    private LocalDate birthday; // 생년월일
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별
    private LocalDate createdAt; // 가입일
    private LocalDate deletedAt; // 만료일 (예약)

    /** 가변 정보 **/
    @Enumerated(EnumType.STRING)
    private Role role; // 역할
    // 상태 ( 추후 추가 )

    /** 매핑관계 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag; // 태그 정보 (호스트나 게스트 모두 하나의 태그 정보만을 가진다.)

    @OneToOne(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chatroom chatroom;


    /** 생성자 **/
    public Member(){}
    public Member(
            String email,
            String password,
            String nickname,
            String birthday,
            Gender gender
    ){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = LocalDate.parse(birthday);
        this.gender = gender;
        this.role = Role.VIEWER; // 초기 생성시 역할은 Viewer
        this.createdAt = LocalDate.now(); // 가입일 저장
        this.deletedAt = createdAt.plusMonths(3); // 만료일은 가입일로부터 3개월 뒤
    }

    /** 정보 설정 및 수정 메서드 **/
    public void updateAccountInfo(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }
    public void unsetTag(){
        this.tag = null;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void setChatroom(Chatroom chatroom){this.chatroom = chatroom;}
    public void unsetChatroom(){this.chatroom = null;}
}
