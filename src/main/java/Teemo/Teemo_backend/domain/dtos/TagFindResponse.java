package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TagFindResponse {
        private Long tagId;
        private String title;
        private Integer maxNum;
        private Gender targetGender;
        private Integer upperAge;
        private Integer lowerAge;
        private Integer remainingMinutes; // 분
        private String hostNickname;
        private Gender hostGender;
        private Integer hostAge;
}