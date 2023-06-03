package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.MsgPalette;
import Teemo.Teemo_backend.domain.dtos.LocationSendDTO;
import Teemo.Teemo_backend.domain.dtos.MsgSendDTO;
import Teemo.Teemo_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatControllerImpl {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/chatroom/{id}")
    public void chatting(@DestinationVariable("id") Long id, MsgPalette msgPalette) throws Exception{
        String msgType = msgPalette.getMsgType();
        if(msgType.equals("CHAT")){ // 채팅하기
            log.info("CHAT");
            String sender = msgPalette.getSender();
            String content = msgPalette.getContent();
            if(sender==null || sender==null) return;

            MsgSendDTO msgSendDto = new MsgSendDTO(sender,content, String.valueOf(id),LocalDateTime.now());
            simpMessagingTemplate.convertAndSend("/sub/" + id, msgSendDto);
            chatService.saveToMemory(msgSendDto);
        }
        else if(msgType.equals("SAVE")){ // db에 저장하기
            log.info("SAVE");
            chatService.saveToDB();
        }
        else if(msgType.equals("NAVIGATE")){ // 내 위치 전송
            log.info("NAVIGATE");
            String latitude = msgPalette.getLatitude();
            String longitude = msgPalette.getLongitude();
            if(latitude.isEmpty() || longitude.isEmpty()) return;
            LocationSendDTO locationSendDTO = new LocationSendDTO(latitude,longitude,String.valueOf(id));
            simpMessagingTemplate.convertAndSend("/sub/"+id,locationSendDTO);
        }

    }
}
