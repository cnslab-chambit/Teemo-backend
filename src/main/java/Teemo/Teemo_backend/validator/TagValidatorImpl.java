package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Gender;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements TagValidator {
    private final double MaxLatitude = 38.61;
    private final double MinLatitude = 33.11;
    private final double MaxLongitude = 131.87;
    private final double MinLongitude = 124.60;
    private final int MaxTitleLength = 15;
    private final int MinTitleLength = 1;
    private final int MaxDetailLength = 40;
    private final int MaxMaxNum = 5;
    private final int MinMaxNum = 1;
    private final int MaxUpperAge = 100;
    private final int MinLowerAge = 0;

    @Override
    public boolean checkLatitude(Double latitude){
        if(MinLatitude<=latitude && latitude <= MaxLatitude) return true;
        return false;
    }
    @Override
    public boolean checkLongitude(Double longitude){
        if(MinLongitude<=longitude && longitude <= MaxLongitude) return true;
        return false;
    }
    @Override
    public boolean checkTitleLength(Integer titleLength) {
        if(MinTitleLength<=titleLength && titleLength<=MaxTitleLength)return true;
        return false;
    }
    @Override
    public boolean checkDetailLength(Integer detailLength) {
        if(detailLength==null) return true;
        if(detailLength<=MaxDetailLength)return true;
        return false;
    }
    @Override
    public boolean checkMaxNum(Integer maxNum) {
        if(MinMaxNum<=maxNum && maxNum<=MaxMaxNum) return true;
        return false;
    }
    @Override
    public boolean compareUpperAge(Integer upperAge, Integer memberAge) { // (자신의 나이) 이상, 100 살 이하
        if(memberAge<=upperAge && upperAge <= MaxUpperAge ) return true;
        return false;
    }
    @Override
    public boolean compareLowerAge(Integer lowerAge, Integer memberAge) { // 0 살 이상, (자신의 나이) 이하
        if(MinLowerAge<=lowerAge && lowerAge <= memberAge ) return true;
        return false;
    }
    @Override
    public boolean checkTargetGender(Gender targetGender){
        if(targetGender == Gender.M || targetGender == Gender.W ||targetGender == Gender.N ) return true;
        return false;
    }
}