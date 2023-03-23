package webSocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void chatting(Message message) throws Exception{
        String sender = message.getSender();
        String content = message.getContent();
        String chatRoomId = message.getChatRoomId();

        log.info("sender={}",sender);
        log.info("content={}",content);
        log.info("chatting_id={}",chatRoomId);

        /*
         * 추후에 수정 예정
         * sender,content,chatting_id 가 중 하나라도 없으면 씹음.
         */
        if(sender.isEmpty() && content.isEmpty() && chatRoomId.isEmpty())
            return;

        /*
         추후에 Message 저장로직을 심을 것
         */

        simpMessagingTemplate.convertAndSend(
                "/sub/"+chatRoomId,
                new Message(sender,content)
        );
    }
}
