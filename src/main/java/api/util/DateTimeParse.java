package api.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class DateTimeParse {
    public static int calculateAge(LocalDate birthday){
        return Period.between(birthday, LocalDate.now()).getYears();
    }
    public static int getRemainingInMinutes(LocalDateTime createAt) {
        long duration = Duration.between(createAt, LocalDateTime.now()).toMinutes();
        return (int) Math.max(0, 60 - duration);
    }
}
