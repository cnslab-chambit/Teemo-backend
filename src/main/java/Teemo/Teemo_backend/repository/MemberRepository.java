package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Member;

public interface MemberRepository {
    void save(Member member);   // 저장
    Member findById(Long id); // id 로 검색
    Member findByEmail(String email);
    Member findByNickname(String nickname); // 닉네임으로 검색
    void delete(Member member); // 삭제
}
