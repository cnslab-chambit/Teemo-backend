package Teemo.Teemo_backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatroomCreateRequest {
    private Long memberId;      // Guest 의 아이디
    private Long tagId;         // Tag 의 아이디
    private Double latitude;    // Guest 의 현재 위도
    private Double longitude;   // Guest 의 현재 경도
}
