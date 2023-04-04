package api.controller;

import api.domain.dtos.CreateChatroomRequest;
import api.domain.dtos.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatroomController {

    /**
     * chatroom 생성
     *
     * 0. 조회자의 현재 신분이 Guest 가 맞는지 확인
     * 1. 유효한 태그인지 확인
     * 2. 태그를 통해 호스트 정보를 확인
     * 3. 호스트의 hostedChatrooms 크기가 10 이하인지 확인
     * 4. 정상 확인 시, 새 chatroom 객체를 생성
     * 5. guest 의 guestChatroom 에 매핑
     * 6. host 의 hostedChatrooms 에 추가
     * 7.
     */
//    @PostMapping("/chatrooms")
//    public ResponseEntity createChatroom(@RequestBody CreateChatroomRequest request){
//
//    }

    /**
     * chatroom 삭제
     *
     *
     */

}
