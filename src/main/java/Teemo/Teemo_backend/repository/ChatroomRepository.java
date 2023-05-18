package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chatroom;

import java.util.List;

public interface ChatroomRepository {
    Long save(Chatroom chatroom); // 저장
    Chatroom findById(Long chatroomId);// id 로 검색
    Long delete(Chatroom chatroom);// 삭제

}
