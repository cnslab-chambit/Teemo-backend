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
         * [체크리스트]
         * 1. 이메일 중복 확인
         * 2. 닉네임 중복 확인
         * 3. 이메일 길이 조건 확인 : [5 ~ 100] 자 사이의 길이를 가져야 한다. (5자, 100자 포함)
         * 4. 비밀번호 길이 조건 확인 : [64] 자 길이를 가져야 한다.
         * 5. 닉네임 길이 조건 확인 : [2 ~ 10] 자 사이의 길이를 가져야 한다. (경계값 포함)
         * 6. 생년월일 이 유효한지 확인:
         *  6-1. "oooo-oo-oo" 형식으로 전달되어야 한다.
         *  6-2. 금일 기준 100 년 전 ~ 금일 ) 사이의 값을 가져야 한다. (경계값 미포함)
         */
        Member find = memberRepository.findByEmailOrNickname(email,nickname);
        // [체크리스트 1,2]
        if(commonValidator.found(find)) // 있으면 오류
            throw new CustomInvalidValueException("email or nickname", "이메일 혹은 닉네임 이 이미 존재합니다.");
        // [체크리스트 3]
        if(!memberValidator.checkEmailLength(email))
            throw new CustomInvalidValueException("email","이메일은 5자 에서 100자 이내로 입력되어야 합니다.");
        // [체크리스트 4]
        if(!memberValidator.checkPasswordLength(password))
            throw new CustomInvalidValueException("password","비밀번호는 64자 길이를 가져야 합니다.");
        // [체크리스트 5]
        if(!memberValidator.checkNicknameLength(nickname))
            throw new CustomInvalidValueException("nickname","닉네임은 2자 에서 10자 이내로 입력되어야 합니다.");
        // [체크리스트 6-1]
        if(!memberValidator.checkBirthdayExpression(birthday))
            throw new CustomInvalidValueException("birthday","생년월일의 형식이 잘못 되었습니다.");
        // [체크리스트 6-2]
        if(!memberValidator.checkBirthdayAssertion(birthday))
            throw new CustomInvalidValueException("birthday","생년월일의 범위가 잘못되었습니다.");
        Member member = new Member(email,password,nickname,birthday,gender);
        memberRepository.save(member);
    }

    @Override
    public Member find(Long memberId) {
        /**
         * [체크리스트]
         * 1. 유효한 사용자인지 확인
         */
        Member member = memberRepository.findById(memberId);
        // [체크리스트 1]
        if(!commonValidator.found(member)) // 없으면 오류 반환.
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        return member;
    }

    @Override
    @Transactional
    public void update(MemberUpdateRequest request) {
        /**
         * [체크리스트]
         * 1. 유효한 사용자인지 확인
         * 2. 이메일 중복 확인
         * 3. 닉네임 중복 확인
         * 4. 이메일 길이 조건 확인 : [5 ~ 100] 자 사이의 길이를 가져야 한다. (5자, 100자 포함)
         * 5. 비밀번호 길이 조건 확인 : [64] 자 길이를 가져야 한다.
         * 6. 닉네임 길이 조건 확인 : [2 ~ 10] 자 사이의 길이를 가져야 한다. (경계값 포함)
         */
        Long id = request.getMemberId();
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        Member member = memberRepository.findById(id);
        // [체크리스트 1]
        if(!commonValidator.found(member)) // 없으면 오류
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        Member find = memberRepository.findByEmailOrNickname(email,nickname);
        // [체크리스트 2,3]
        if(commonValidator.found(find)) // 있으면 오류
            throw new CustomInvalidValueException("email or nickname", "이메일 혹은 닉네임 이 이미 존재합니다.");
        // [체크리스트 4]
        if(!memberValidator.checkEmailLength(email))
            throw new CustomInvalidValueException("email","이메일은 5자 에서 100자 이내로 입력되어야 합니다.");
        // [체크리스트 5]
        if(!memberValidator.checkPasswordLength(password))
            throw new CustomInvalidValueException("password","비밀번호는 64자 길이를 가져야 합니다.");
        // [체크리스트 6]
        if(!memberValidator.checkNicknameLength(nickname))
            throw new CustomInvalidValueException("nickname","닉네임은 2자 에서 10자 이내로 입력되어야 합니다.");
        member.updateAccountInfo(email,password,nickname);
    }

    @Override
    @Transactional
    public void remove(Long memberId) {
        /**
         * [체크리스트]
         * 1. 유효한 사용자인지 확인
         */
        Member member = memberRepository.findById(memberId);
        // [체크리스트 1]
        if(!commonValidator.found(member)) // 없으면 오류 반환.
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        memberRepository.delete(member);
    }

    @Override
    public Member login(MemberLoginRequest request) {
        /**
         * [체크리스트]
         * 1. 유효한 사용자인지 확인
         * 2. 비밀번호가 일치하는지 확인
         * 3. 기간이 만료된 회원인지 확인 (-> 완료되었다면 삭제)
         */
        String email = request.getEmail();
        String password = request.getPassword();
        Member find = memberRepository.findByEmail(email);
        // [체크리스트 1]
        if(!commonValidator.found(find)) // 없으면 오류
            throw new CustomInvalidValueException("email","회원이 식별되지 않습니다.");
        // [체크리스트 2]
        if(!memberValidator.comparePassword(password,find.getPassword())){
            throw new CustomInvalidValueException("password","비밀번호가 틀렸습니다.");
        }
        // [체크리스트 3]
        if(!memberValidator.checkDeadLine(LocalDate.now(),find.getDeletedAt())){
            memberRepository.delete(find);
            throw new CustomInvalidValueException("deletedAt","삭제된 회원입니다.");
        }
        return find;
    }

    @Override
    @Transactional
    public void logout(Long memberId) {
        /**
         * [체크리스트]
         * 1. 유효한 사용자인지 확인
         *  1-1. 사용자의 역할이 "HOST" 라면 Tag 를 제거해준다. (Tag 제거가 안되면, 오류 반환)
         *  1-2. 사용자의 역할이 "GUEST" 라면 Tag 구독을 취소해준다. (Tag 구독 취소가 안되면, 오류반환)
         *
         */
        Member findMember = memberRepository.findById(memberId);
        // [체크리스트 1]
        if(!commonValidator.found(findMember))
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        // [체크리스트 1-1]
        if(findMember.getRole() == Role.HOST) {
            Long tagId = findMember.getTag().getId();
            try {
                tagService.remove(memberId, tagId);
            }catch (Exception e){
                throw new CustomInvalidValueException("tagId","Tag 를 제거해야 합니다.");
            }
        }
        // [체크리스트 1-2]
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
