package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Chat;
import Teemo.Teemo_backend.domain.dtos.ChatroomCreateRequest;
import Teemo.Teemo_backend.domain.dtos.ChatroomSearchResponse;

import java.util.List;

public interface ChatroomService {
    ChatroomSearchResponse create(ChatroomCreateRequest request); // 채팅방 생성
    List<ChatroomSearchResponse> search(Long memberId, Long tagId); // 참여 가능한 채팅방 검색
    List<Chat> load(Long chatroomId);// 특정 채팅방 입장 ( = 채팅 내역 불러오기 )
    void remove(Long memberId,Long chatroomId);// 채팅방 삭제
}
