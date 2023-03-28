package api.domain.dtos;

import api.domain.Gender;
import lombok.Data;

@Data
public class CreateTagRequestDTO {
    private Long hostId;
    private double latitude;
    private double longitude;
    private String title;
    private String detail;
    private int limit;
    private Gender targetGender;
    private Integer targetAgeUpper;
    private Integer targetAgeLower;
}
