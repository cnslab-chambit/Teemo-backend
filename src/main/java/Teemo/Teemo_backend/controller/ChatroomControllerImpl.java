package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Chat;
import Teemo.Teemo_backend.domain.dtos.ChatroomCreateRequest;
import Teemo.Teemo_backend.domain.dtos.ChatroomSearchResponse;
import Teemo.Teemo_backend.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
@Slf4j
public class ChatroomControllerImpl {
    private final ChatroomService chatroomService;

    /**
     * 채팅방 생성
     *
     * @input   :   memberId, tagId, latitude, longitude
     * @output  :   chatroomId,nickname,age,gender,title
     */
    @PostMapping("/create")
    public ResponseEntity<ChatroomSearchResponse> createChatroom(@RequestBody ChatroomCreateRequest request){
        log.info("memberId: "+request.getMemberId());
        log.info("tatId: "+request.getTagId());
        log.info("latitude: "+ request.getLatitude());
        log.info("longitude: "+request.getLongitude());
        ChatroomSearchResponse response = chatroomService.create(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 참여 가능한 채팅방 검색
     *
     * @input   :   memberId,tagId
     * @output  :   [ {chatroomId,nickname,age,gender,title},....,{chatroomId,nickname,age,gender,title} ]
     */
    @GetMapping("/search")
    public ResponseEntity<List<ChatroomSearchResponse>> searchChatroom(
            @RequestParam Long memberId,
            @RequestParam Long tagId
    ){
        List<ChatroomSearchResponse> responses = chatroomService.search(memberId, tagId);
        return ResponseEntity.ok(responses);
    }

    /**
     * 특정 채팅방 입장 ( = 채팅 내역 불러오기 )
     *
     * @input   : chatroomId
     * @output  : [{chatId, nickname, msg} , ... , {chatId, nickname, msg}]
     */
    @GetMapping("/enter")
    public ResponseEntity<List<Chat>> enterChatroom(@RequestParam Long chatroomId){
        List<Chat> response = chatroomService.load(chatroomId);
        return ResponseEntity.ok(response);
    }

    /**
     * 채팅방 삭제
     *
     * @input   : chatroomId
     * @output  : HttpStatus.NO_CONTENT
     */
    @DeleteMapping("/delete/{memberId}/{chatroomId}")
    public ResponseEntity deleteChatroom(
            @PathVariable Long memberId,
            @PathVariable Long chatroomId
    )
    {
        chatroomService.remove(memberId, chatroomId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
