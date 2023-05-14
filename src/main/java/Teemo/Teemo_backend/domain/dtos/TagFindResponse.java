package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;
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

        public TagFindResponse(
                Long tagId,
                String title,
                Integer maxNum,
                Gender targetGender,
                Integer upperAge,
                Integer lowerAge,
                Integer remainingMinutes, // 분
                String hostNickname,
                Gender hostGender,
                Integer hostAge
        ){
                this.tagId = tagId;
                this.title = title;
                this.maxNum = maxNum;
                this.targetGender = targetGender;
                this.upperAge = upperAge;
                this.lowerAge = lowerAge;
                this.remainingMinutes = remainingMinutes;
                this.hostNickname = hostNickname;
                this.hostGender = hostGender;
                this.hostAge = hostAge;
        }
}