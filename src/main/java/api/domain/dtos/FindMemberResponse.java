package api.domain.dtos;

import api.domain.Gender;
import api.domain.Member;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindMemberResponse {
    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private String email;

    public FindMemberResponse(Member member){
        this.nickname = member.getNickname();
        this.birthday = member.getBirthday();
        this.gender = member.getGender();
        this.email = member.getEmail();
    }
}
