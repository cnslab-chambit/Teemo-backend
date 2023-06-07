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
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    ConcurrentHashMap<Long, MsgSendDTO> msgMap = new ConcurrentHashMap<>();
    @Override
    public void saveToMemory(Object obj) {
        if (obj instanceof MsgSendDTO) {
            MsgSendDTO msg = (MsgSendDTO) obj;
            LocalDateTime timestamp = msg.getTimestamp();

            // 시분초 를 모두 초 단위로 바꾸어 더한 값
            // 항상 5자리 문자열
            String time = String.format("%05d", timestamp.getHour() * 60 * 60 + timestamp.getMinute() * 60 + timestamp.getSecond());

            String chatroomId = msg.getChatroomId();
            Long chatId = generateHash(chatroomId,time);
            msgMap.put(chatId,msg);
        }
    }
    @Override
    @Transactional
    public void saveToDB() {
        if (msgMap.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, MsgSendDTO> entry : msgMap.entrySet()) {
            Long chatId = entry.getKey();
            MsgSendDTO msg = entry.getValue();

            String sender = msg.getSender();
            String content = msg.getContent();
            Long chatroomId = Long.parseLong(msg.getChatroomId());

            // Chatroom 조회
            Chatroom chatroom = chatroomRepository.findById(chatroomId);
            if (chatroom != null) {
                // Chat 생성 및 저장
                Chat chat = Chat.createChat(chatId, content, sender, chatroom);
                chatRepository.save(chat);
            }
            // 저장이 완료된 데이터는 map에서 삭제
            msgMap.remove(chatId);
        }
    }
    public Long generateHash(String s1, String s2){  // s1: chatroomId, s2: timestamp
        /**
         13자리의 유일한 해쉬값을 반환합니다.

         처음  5자리: chatroomId 의 역순, 5자리가 안될 경우 끝부분을 0으로 패딩
         중간  5자리: timestamp 으 시,분,초 를 초 단위로 환산하여 합한 값, 5자리가 안될 경우 앞부분에 0으로 패딩
         끝에  3자리: 3자리 난수
         */
        // chatroomId를 뒤집기 위해 StringBuilder를 사용합니다.
        StringBuilder reversedIdBuilder = new StringBuilder(s1).reverse();
        // 10000의 자리가 안되는 경우 0을 추가해줍니다.
        while (reversedIdBuilder.length() < 5) {
            reversedIdBuilder.append("0");
        }
        // 뒤집어진 chatroomId의 앞 5자리를 추출하고, 그 뒤에 timestamp 를 추가한다.
        String reversedId = reversedIdBuilder.substring(0, 5).concat(s2);

        // 추가로 3자리 난수를 생성하여 가장 뒤에 자리에 더한다.
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;

        Long result = Long.parseLong(reversedId) * 1000 + Long.valueOf(randomNumber);
        return result;
    }
}
