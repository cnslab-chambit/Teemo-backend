package Teemo.Teemo_backend.domain.dtos;
import lombok.Getter;

@Getter
public class ChatroomEnterResponse {
    private String timestamp;
    private String msg; // 메시지 (50자 제한)
    private String sender; // 송신자
    public ChatroomEnterResponse(Long id, String msg, String sender){
        this.msg = msg;
        this.sender = sender;
        setTimestamp(id);
    }
    private void setTimestamp(Long id){
        String time = String.valueOf(id).substring(5,10);
        int seconds = Integer.parseInt(time);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = (seconds % 3600) % 60;
        this.timestamp = new StringBuilder()
                .append(hours)
                .append(":")
                .append(minutes)
                .append(":")
                .append(remainingSeconds)
                .toString();
    }

}
