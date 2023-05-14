package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chatroom;

import java.util.List;

public interface ChatroomRepository {
    Long save(); // 저장
    Chatroom findById();// id 로 검색
    List<Chatroom> findAll();// 전체 불러오기
    Long delete();// 삭제

}
