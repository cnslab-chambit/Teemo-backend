package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatroomSearchResponse {
    private Long chatroomId;
    private String nickname;
    private Integer age;
    private Gender gender;
    private String title;
}