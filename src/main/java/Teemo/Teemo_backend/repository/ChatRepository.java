package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chat;

import java.util.List;

public interface ChatRepository {
    Long save(Chat chat); // 저장
}
