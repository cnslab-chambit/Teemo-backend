package Teemo.Teemo_backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberUpdateRequest {
    private Long id;
    private String email;
    private String password;
    private String nickname;
}
