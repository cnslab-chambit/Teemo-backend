package api.service;

import api.domain.Member;
import api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member){
        return memberRepository.save(member);
    }
    public Member findById(Long memberId){
        return memberRepository.find(memberId);
    }
    public void updateEmail(Long memberId,String email){
        Member member = memberRepository.find(memberId);
        member.setEmail(email);
    }
    public void updatePassword(Long memberId, String password){
        Member member = memberRepository.find(memberId);
        member.setPassword(password);
    }

    public void updateNickname(Long memberId, String nickname){
        Member member = memberRepository.find(memberId);
        member.setNickname(nickname);
    }

}
