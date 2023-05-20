package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TagCreateRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private String title;
    private String detail;
    @NotNull
    private Integer maxNum;
    @NotNull
    private Gender targetGender;
    @NotNull
    private Integer upperAge;
    @NotNull
    private Integer lowerAge;
}
