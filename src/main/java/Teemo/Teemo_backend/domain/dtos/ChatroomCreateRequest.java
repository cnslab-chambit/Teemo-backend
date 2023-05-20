package Teemo.Teemo_backend.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatroomCreateRequest {
    @NotNull
    private Long memberId;      // Guest 의 아이디
    @NotNull
    private Long tagId;         // Tag 의 아이디
    @NotNull
    private Double latitude;    // Guest 의 현재 위도
    @NotNull
    private Double longitude;   // Guest 의 현재 경도
}
