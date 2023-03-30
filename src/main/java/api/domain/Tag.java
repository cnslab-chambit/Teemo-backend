package api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Tag {
    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToMany(mappedBy = "tag",fetch = FetchType.LAZY)
    private List<Member> guests = new ArrayList<>();

    // 모집 정보
    private String title;   // 타이틀
    private String detail;  // 상세설명
    private int limit;
    private Gender targetGender; // 모집 성별
    private Integer targetAgeUpper;  // 모집 나이 상한
    private Integer targetAgeLower; //모집 나이 하한
    private LocalDateTime createAt; // tag 생성 시간

    // 위치 정보
    private double latitude; // 위도
    private double longitude; // 경도

    //==기본 생성자==//
    public Tag() {}

    //==(위치 + 모집) 정보를 받을 생성자==//
    public Tag(
            String title,
            String detail,
            int limit,
            Gender targetGender,
            Integer targetAgeUpper,
            Integer targetAgeLower,
            double latitude,
            double longitude
            )
    {

        this.title = title;
        this.detail = detail;
        this.limit = limit;
        this.targetGender = targetGender;
        this.targetAgeUpper = targetAgeUpper;
        this.targetAgeLower = targetAgeLower;

        this.latitude = latitude;
        this.longitude = longitude;

        this.createAt = LocalDateTime.now();
    }

    //==연관관계 메서드==//
    public void setHost(Member host) {
        this.host = host;
        host.setTag(this);
        host.setRole(Role.HOST);
    }
}

