package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Tag;

public interface TagValidator {
    boolean checkLatitude(Double latitude);
    boolean checkLongitude(Double longitude);
    boolean checkTitleLength(Integer titleLength);
    boolean checkDetailLength(Integer detailLength) ;
    boolean checkMaxNum(Integer maxNum);
    boolean checkUpperAge(int upperAge, int memberAge);
    boolean checkLowerAge(int lowerAge, int memberAge);
    boolean checkTargetGender(Gender targetGender);
}
