package Teemo.Teemo_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MsgPalette {
    private String sender;
    private String content;
    private String msgType;
    private String latitude;
    private String longitude;
}
