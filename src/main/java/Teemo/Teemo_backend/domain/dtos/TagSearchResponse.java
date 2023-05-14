package Teemo.Teemo_backend.domain.dtos;

import Teemo.Teemo_backend.domain.Gender;

// tagId, title, maxNum, targetGender, upperAge, lowerAge, remainingTime, hostNickName, hostGender, hostAge
public class TagSearchResponse {
    Long tagId;
    Double latitude;
    Double longitude;

    public TagSearchResponse(Long tagId, Double latitude, Double longitude){
        this.tagId = tagId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
