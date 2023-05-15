package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import lombok.Getter;

@Getter
public class MemberSignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String birthday;
    private Gender gender;
}
