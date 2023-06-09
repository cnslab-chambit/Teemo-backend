package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Gender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TagValidatorImpl implements TagValidator {
    static final double MaxLatitude = 38.61;
    static final double MinLatitude = 33.11;
    static final double MaxLongitude = 131.87;
    static final double MinLongitude = 124.60;
    static final int MaxTitleLength = 15;
    static final int MinTitleLength = 1;
    static final int MaxDetailLength = 40;
    static final int MaxMaxNum = 5;
    static final int MinMaxNum = 1;
    static final int MaxUpperAge = 100;
    static final int MinLowerAge = 0;

    @Override
    public boolean checkLatitude(Double latitude){
        return (MinLatitude<=latitude && latitude <= MaxLatitude);
    }
    @Override
    public boolean checkLongitude(Double longitude){
        return (MinLongitude<=longitude && longitude <= MaxLongitude);
    }
    @Override
    public boolean checkTitleLength(Integer titleLength) {
        return (MinTitleLength<=titleLength && titleLength<=MaxTitleLength);
    }
    @Override
    public boolean checkDetailLength(Integer detailLength) {
        return (detailLength==null||detailLength<=MaxDetailLength);
    }
    @Override
    public boolean checkMaxNum(Integer maxNum) {
        return (MinMaxNum<=maxNum && maxNum<=MaxMaxNum);
    }
    @Override
    public boolean compareUpperAge(Integer upperAge, Integer memberAge) { // (자신의 나이) 이상, 100 살 이하
        return (memberAge<=upperAge && upperAge <= MaxUpperAge );
    }
    @Override
    public boolean compareLowerAge(Integer lowerAge, Integer memberAge) { // 0 살 이상, (자신의 나이) 이하
        return (MinLowerAge<=lowerAge && lowerAge <= memberAge );
    }

    @Override
    public boolean checkDeadLine(LocalDateTime now, LocalDateTime deletedAt) {
        return now.isBefore(deletedAt);
    }

    @Override
    public boolean checkTargetGender(Gender targetGender){
        return (targetGender == Gender.M || targetGender == Gender.W ||targetGender == Gender.N );
    }
}