package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.User;
import Teemo.Teemo_backend.domain.dtos.UserSignupRequest;
import Teemo.Teemo_backend.domain.dtos.UserUpdateRequest;

public interface UserService {
    void join(UserSignupRequest request); // 회원가입
    User find(Long id);// 회원조회
    void update(UserUpdateRequest userUpdateRequest);// 정보 수정
    void remove(Long id);// 회원탈퇴
}
