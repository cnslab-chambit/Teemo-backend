package api.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeParse {
    public static int  calculateAge(LocalDate birthday){
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
