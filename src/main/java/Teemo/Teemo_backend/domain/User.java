package Teemo.Teemo_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    /** 가입 정보 **/
    private String email; // 이메일 주소
    private String password; // 비밀번호
    @Column(length = 10)
    private String nickname; // 닉네임
    @JsonFormat(pattern = "yyyy-mm-dd")
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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private List<Chatroom> chatrooms = new ArrayList<>(); // 참여 중인 채팅방 리스트(호스트는 10개, 게스트 1개 의 원소를 가질 수 있다.)

    /** 생성자 **/
    public User(){}
    public User(
            String email,
            String password,
            String nickname,
            LocalDate birthday,
            Gender gender
    ){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        createdAt = LocalDate.now(); // 가입일 저장
        deletedAt = createdAt.plusMonths(3); // 만료일은 가입일로부터 3개월 뒤
    }

}
