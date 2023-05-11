package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.User;

import java.util.List;

public interface UserRepository {
    Long save(User user);   // 저장
    User findById(Long id); // id 로 검색
    Long delete(User user); // 삭제
}
