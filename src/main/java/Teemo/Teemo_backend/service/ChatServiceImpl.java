package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Chat;
import Teemo.Teemo_backend.domain.Chatroom;
import Teemo.Teemo_backend.domain.dtos.MsgSendDTO;
import Teemo.Teemo_backend.repository.ChatRepository;
import Teemo.Teemo_backend.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    ConcurrentHashMap<Long, MsgSendDTO> msgMap = new ConcurrentHashMap<>();
    @Override
    public void saveToMemory(Object obj) {
        log.info("saveToMemory");
        if (obj instanceof MsgSendDTO) {
            MsgSendDTO msg = (MsgSendDTO) obj;
            LocalDateTime timestamp = msg.getTimestamp();

            String time = String.valueOf(
                    timestamp.getHour()*60*60
                    +timestamp.getMinute()*60
                    +timestamp.getSecond());

            String chatroomId = msg.getChatroomId();
            Long chatId = generateHash(chatroomId,time);
            log.info("chatId={}",chatId);
            msgMap.put(chatId,msg);
        }
    }
    @Override
    @Transactional
    public void saveToDB() {
        log.info("saveToDB");
        if (msgMap.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, MsgSendDTO> entry : msgMap.entrySet()) {
            Long chatId = entry.getKey();
            MsgSendDTO msg = entry.getValue();

            String sender = msg.getSender();
            String content = msg.getContent();
            String chatroomId = msg.getChatroomId();

            // Chatroom 조회
            Chatroom chatroom = chatroomRepository.findById(Long.parseLong(chatroomId));
            if (chatroom != null) {
                log.info("save chatId={}",chatId);
                // Chat 생성 및 저장
                Chat chat = Chat.createChat(chatId, content, sender, chatroom);
                chatRepository.save(chat);
            }
            log.info("chatId={}",chatId);
            // 저장이 완료된 데이터는 map에서 삭제
            msgMap.remove(chatId);
        }
    }


    public Long generateHash(String s1, String s2){  // s1: chatroomId, s2: timestamp
        // chatroomId를 뒤집기 위해 StringBuilder를 사용합니다.
        StringBuilder reversedIdBuilder = new StringBuilder(s1).reverse();
        // 1000의 자리가 안되는 경우 0을 추가해줍니다.
        while (reversedIdBuilder.length() < 5) {
            reversedIdBuilder.append("0");
        }
        // 뒤집어진 chatroomId의 앞 5자리를 추출하고, 그 뒤에 timestamp 를 추가한다.
        String reversedId = reversedIdBuilder.substring(0, 5).concat(s2);

        // 추가로 3자리 난수를 생성하여 더한다.
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;
        reversedId = reversedId.concat(String.valueOf(randomNumber));

        return Long.parseLong(reversedId);
    }
}
