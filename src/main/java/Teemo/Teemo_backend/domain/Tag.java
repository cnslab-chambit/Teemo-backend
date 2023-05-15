package Teemo.Teemo_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

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
    private Double latitude; // 태그 생성 위도
    private Double longitude; // 태크 생성 경도
    private LocalDateTime createdAt; // tag 생성 시간
    private LocalDateTime deletedAt; // tag 삭제 시간

    /** 매핑관계 **/
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();  // 인덱스 0 은 호스트 정보, 나머지는 게스트 정보

    /** 생성자 **/
    public Tag(){};
    public Tag(
            String title,
            String detail,
            Integer maxNum,
            Gender targetGender,
            Integer upperAge,
            Integer lowerAge,
            Double latitude,
            Double longitude,
            Member host
    ){
        this.title = title;
        this.detail = detail;
        this.maxNum = maxNum;
        this.targetGender = targetGender;
        this.upperAge = upperAge;
        this.lowerAge = lowerAge;
        this.latitude = latitude;
        this.longitude = longitude;
        this.members.add(host);
        host.setRole(Role.HOST); // 호스트 역할 부여
        host.setTag(this); // 호스트 태그 등록
        createdAt = LocalDateTime.now(); // 태그 생성 시, 태그 생성 시간 저장 (서버기준)
        deletedAt = createdAt.plusHours(1); // 태그 삭제 시간은 생성 시간으로부터 1시간 뒤
    }
    public void addGuest(Member member){
        member.setTag(this);
        member.setRole(Role.GUEST);
        this.members.add(member);
    }
    public void removeGuest(Member member){
        member.unsetTag();
        member.setRole(Role.VIEWER);

        for(int i = 1; i<this.members.size(); i++)
            if(members.get(i).equals(member)){
                members.remove(i);
                break;
            }
    }
    public void removeAllMembers(){
        int idx=0;
        for(Member member : this.members){
            member.unsetTag();
            member.setRole(Role.VIEWER);
        }
        members = new ArrayList<>();
    }

}
