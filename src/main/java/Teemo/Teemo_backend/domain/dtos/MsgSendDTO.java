package Teemo.Teemo_backend.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MsgSendDTO {
    private String sender;
    private String content;
    private String chatroomId;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime timestamp;
}
