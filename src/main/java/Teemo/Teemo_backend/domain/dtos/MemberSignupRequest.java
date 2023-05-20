package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberSignupRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickname;
    @NotNull
    private String birthday;
    @NotNull
    private Gender gender;
}
