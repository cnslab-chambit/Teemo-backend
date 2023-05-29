package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;
import Teemo.Teemo_backend.domain.dtos.MemberLoginRequest;
import Teemo.Teemo_backend.domain.dtos.MemberSignupRequest;
import Teemo.Teemo_backend.domain.dtos.MemberUpdateRequest;
import Teemo.Teemo_backend.error.CustomInvalidValueException;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.validator.CommonValidator;
import Teemo.Teemo_backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberValidator memberValidator;
    private final CommonValidator commonValidator;

    private final MemberRepository memberRepository;

    private final TagService tagService;

    @Override
    @Transactional
    public void join(MemberSignupRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();
        String birthday = request.getBirthday();
        Gender gender = request.getGender();

        /**
         * [제약조건]
         * 1. 빈 값 확인
         * 2. 이메일 중복 확인
         * 3. 닉네임 중복 확인
         * 4. 이메일 길이 조건 확인 : [5 ~ 100] 자 사이의 길이를 가져야 한다. (5자, 100자 포함)
         * 5. 비밀번호 길이 조건 확인 : [15] 자 길이를 가져야 한다.
         * 6. 닉네임 길이 조건 확인 : [2 ~ 10] 자 사이의 길이를 가져야 한다. (경계값 포함)
         * 7. 생년월일 조건 확인 : ( 금일 기준 100 년 전 ~ 금일 ) 사이의 값을 가져야 한다. (경계값 미포함)
         */
        Member member = new Member(email,password,nickname,birthday,gender);
        memberRepository.save(member);
    }

    @Override
    public Member find(Long memberId) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */

        Member member = memberRepository.findById(memberId);
        return member;
    }

    @Override
    @Transactional
    public void update(MemberUpdateRequest request) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */
        Long id = request.getId();
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        Member member = memberRepository.findById(id);
        member.updateAccountInfo(email,password,nickname);
    }

    @Override
    @Transactional
    public void remove(Long memberId) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */
        Member member = memberRepository.findById(memberId);
        memberRepository.delete(member);
    }

    @Override
    public Member login(MemberLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        Member findMember = memberRepository.findByEmail(email);

        if(!commonValidator.found(findMember))
            throw new CustomInvalidValueException("email","회원이 식별되지 않습니다.");
        if(!memberValidator.comparePassword(password,findMember.getPassword())){
            throw new CustomInvalidValueException("password","비밀번호가 틀렸습니다.");
        }
        if(!memberValidator.checkDeadLine(LocalDate.now(),findMember.getDeletedAt())){
            memberRepository.delete(findMember);
            throw new CustomInvalidValueException("deletedAt","삭제된 회원입니다.");
        }
        return findMember;
    }

    @Override
    public void logout(Long memberId) {
        Member findMember = memberRepository.findById(memberId);
        if(!commonValidator.found(findMember))
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");

        if(findMember.getRole() == Role.HOST) {
            Long tagId = findMember.getTag().getId();
            try {
                tagService.remove(memberId, tagId);
            }catch (Exception e){
                throw new CustomInvalidValueException("tagId","Tag 를 제거해야 합니다.");
            }
        }
        else if(findMember.getRole() == Role.GUEST) {
            Long tagId = findMember.getTag().getId();
            try {
                tagService.unsubscribe(memberId, tagId);
            }catch (Exception e){
                throw new CustomInvalidValueException("tagId","Tag 구독을 취소해야 합니다.");
            }
        }

    }


}
