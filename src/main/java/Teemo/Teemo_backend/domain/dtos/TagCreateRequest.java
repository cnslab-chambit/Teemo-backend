package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import lombok.Getter;

@Getter
public class TagCreateRequest {
    private Long memberId;
    private Double latitude;
    private Double longitude;
    private String title;
    private String detail;
    private Integer maxNum;
    private Gender targetGender;
    private Integer upperAge;
    private Integer lowerAge;
}
