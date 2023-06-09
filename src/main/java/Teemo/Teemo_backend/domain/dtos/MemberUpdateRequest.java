package Teemo.Teemo_backend.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberUpdateRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickname;
}
