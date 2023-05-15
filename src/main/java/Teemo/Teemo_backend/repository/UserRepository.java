package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);   // 저장
    User findById(Long id); // id 로 검색
    User findByEmail(String email);
    User findByNickname(String nickname); // 닉네임으로 검색
    void delete(User user); // 삭제
}
