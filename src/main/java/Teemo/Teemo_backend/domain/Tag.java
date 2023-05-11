package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id; //DB 탐색용 식별자

    /** 모집정보 **/
    @Column(length = 15)
    private String title; // 모집 제목
    @Column(length = 40)
    private String detail; // 모집 상세 설명
    private Integer maxNum; // 최대 모집 인원수
    @Enumerated(EnumType.STRING)
    private Gender targetGender; // 모집 성별
    private Integer upperAge; // 모집 나이 상한
    private Integer lowerAge; // 모집 나이 하한

    /** 태그정보 **/
    private double latitude; // 태그 생성 위도
    private double longitude; // 태크 생성 경도
    private LocalDateTime createdAt; // tag 생성 시간
    private LocalDateTime deletedAt; // tag 삭제 시간

    /** 매핑관계 **/
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private List<User> users = new ArrayList<>();  // 인덱스 0 은 호스트 정보, 나머지는 게스트 정보


    /** 생성자 **/
    public Tag(){};
    public Tag(
            String title,
            String detail,
            int maxNum,
            Gender targetGender,
            Integer upperAge,
            Integer lowerAge,
            double latitude,
            double longitude,
            User host
    ){
        this.title = title;
        this.detail = detail;
        this.maxNum = maxNum;
        this.targetGender = targetGender;
        this.upperAge = upperAge;
        this.lowerAge = lowerAge;
        this.latitude = latitude;
        this.longitude = longitude;
        this.users.add(host);

        createdAt = LocalDateTime.now(); // 태그 생성 시, 태그 생성 시간 저장 (서버기준)
        deletedAt = createdAt.plusHours(1); // 태그 삭제 시간은 생성 시간으로부터 1시간 뒤
    }

}
