package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Role;
import Teemo.Teemo_backend.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberLoginResponse {
    private Long memberId;
    private Role role;
}
