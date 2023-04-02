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

}
