package api.domain.dtos;

import api.domain.Gender;
import api.domain.Member;
import api.domain.Tag;
import api.util.DateTimeParse;

public class SearchTagResponse {
    private Long id; // Tag Id

    private String title;
    private String detail;
    private int limit;
    private int targetAgeUpper;
    private int targetAgeLower;
    private Gender targetGender;

    private int remainingTime;

    private String nickName; // host 의 닉네임
    private Gender gender; // host 의 성별
    private int age; // host 의 나이

    public SearchTagResponse(Tag tag, Member member){
        this.id = tag.getId();
        this.title = tag.getTitle();
        this.detail = tag.getDetail();
        this.limit = tag.getLimit();
        this.targetAgeUpper = tag.getTargetAgeUpper();
        this.targetAgeLower = tag.getTargetAgeLower();
        this.targetGender = tag.getTargetGender();
        this.remainingTime = DateTimeParse.getRemainingInMinutes(tag.getCreateAt());
        this.nickName = member.getNickname();
        this.gender = member.getGender();
        this.age = DateTimeParse.calculateAge(member.getBirthday());
    }
}
