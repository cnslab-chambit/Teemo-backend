package Teemo.Teemo_backend.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LocationSendDTO {
    private String latitude;
    private String longitude;
    private String chatroomId;
}
