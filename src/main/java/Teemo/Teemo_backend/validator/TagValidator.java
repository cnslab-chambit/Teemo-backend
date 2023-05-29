package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Gender;

public interface TagValidator {
    boolean checkLatitude(Double latitude);
    boolean checkLongitude(Double longitude);
    boolean checkTitleLength(Integer titleLength);
    boolean checkDetailLength(Integer detailLength) ;
    boolean checkMaxNum(Integer maxNum);
    boolean checkTargetGender(Gender targetGender);
    boolean compareUpperAge(Integer upperAge, Integer memberAge);
    boolean compareLowerAge(Integer lowerAge, Integer memberAge);
}
