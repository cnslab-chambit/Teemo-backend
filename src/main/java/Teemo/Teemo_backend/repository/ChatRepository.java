package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chat;

import java.util.List;

public interface ChatRepository {
    Long save(); // 저장
    List<Chat> findAll(); // 전체 불러오기
    Long delete(); // 삭제
}
