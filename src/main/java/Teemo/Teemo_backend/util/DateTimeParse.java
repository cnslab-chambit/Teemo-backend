package Teemo.Teemo_backend.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeParse {
    /** 생년월일 -> 만 나이 */
    public static Integer calculateAge(LocalDate birthday){
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public static Integer getRemainingInMinutes(LocalDateTime futureDateTime){
        long duration = Duration.between(LocalDateTime.now(),futureDateTime).toMinutes();
        return (int) Math.max(0, 60 - duration);

    }
}
