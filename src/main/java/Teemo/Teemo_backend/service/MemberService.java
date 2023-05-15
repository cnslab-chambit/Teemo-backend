package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.dtos.MemberSignupRequest;
import Teemo.Teemo_backend.domain.dtos.MemberUpdateRequest;

public interface MemberService {
    void join(MemberSignupRequest request); // 회원가입
    Member find(Long id);// 회원조회
    void update(MemberUpdateRequest memberUpdateRequest);// 정보 수정
    void remove(Long id);// 회원탈퇴
}
