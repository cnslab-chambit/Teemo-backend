package Teemo.Teemo_backend.domain.dtos;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private Long id;
    private String email;
    private String password;
    private String nickname;
}
