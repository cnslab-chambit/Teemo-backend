package api.domain.dtos;

import api.domain.Gender;
import api.domain.Member;
import api.domain.Tag;
import api.util.DateTimeParse;
import lombok.Data;

@Data
public class SearchTagResponse {
    private Long tagId; // Tag Id

    private String title;
    private String detail;
    private int maxNum;
    private int targetAgeUpper;
    private int targetAgeLower;
    private Gender targetGender;

    private int remainingTime;

    private String hostNickName; // host 의 닉네임
    private Gender hostGender; // host 의 성별
    private int hostAge; // host 의 나이

    public SearchTagResponse(Tag tag, Member member){
        this.tagId = tag.getId();
        this.title = tag.getTitle();
        this.detail = tag.getDetail();
        this.maxNum = tag.getMaxNum();
        this.targetAgeUpper = tag.getTargetAgeUpper();
        this.targetAgeLower = tag.getTargetAgeLower();
        this.targetGender = tag.getTargetGender();
        this.remainingTime = DateTimeParse.getRemainingInMinutes(tag.getCreateAt());
        this.hostNickName = member.getNickname();
        this.hostGender = member.getGender();
        this.hostAge = DateTimeParse.calculateAge(member.getBirthday());
    }
}
