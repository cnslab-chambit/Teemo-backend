package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class MemberFindResponse {
    private String email;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
    private String createdAt;
    private String deletedAt;

    public MemberFindResponse(
            String email,
            String password,
            String nickname,
            LocalDate birthday,
            Gender gender,
            LocalDate createdAt,
            LocalDate deletedAt
    ){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.createdAt = createdAt.toString();
        this.deletedAt = deletedAt.toString();
    }
}
