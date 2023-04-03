package api.domain.dtos;

import api.domain.Gender;
import api.domain.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMemberRequest {
    private String email;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
}
