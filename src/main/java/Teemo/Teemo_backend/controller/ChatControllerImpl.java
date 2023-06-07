package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.MsgPalette;
import Teemo.Teemo_backend.domain.dtos.LocationSendDTO;
import Teemo.Teemo_backend.domain.dtos.MsgSendDTO;
import Teemo.Teemo_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/chatroom/{id}")
    public void chatting(@DestinationVariable("id") Long id, MsgPalette msgPalette) throws Exception{
        String msgType = msgPalette.getMsgType();
            if(msgType.equals("CHAT")){ // 채팅하기
            String sender = msgPalette.getSender();
            String content = msgPalette.getContent();
            if(sender==null || sender==null) return;

            MsgSendDTO msgSendDto = new MsgSendDTO(sender,content, String.valueOf(id),LocalDateTime.now());
            simpMessagingTemplate.convertAndSend("/sub/" + id, msgSendDto);
            chatService.saveToMemory(msgSendDto);
        }
        else if(msgType.equals("SAVE")){ // db에 저장하기
            chatService.saveToDB();
        }
        else if(msgType.equals("NAVIGATE")){ // 내 위치 전송
            String latitude = msgPalette.getLatitude();
            String longitude = msgPalette.getLongitude();
            if(latitude.isEmpty() || longitude.isEmpty()) return;
            LocationSendDTO locationSendDTO = new LocationSendDTO(latitude,longitude,String.valueOf(id));
            simpMessagingTemplate.convertAndSend("/sub/"+id,locationSendDTO);
        }

    }
}
