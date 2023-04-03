package api.controller;

import api.domain.Member;
import api.domain.Role;
import api.domain.dtos.CreateMemberRequest;
import api.domain.dtos.FindMemberResponse;
import api.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원 생성
     * 회원을 생성하고 회원 ID를 반환하는 엔드포인트
     */
    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody CreateMemberRequest request){
        Member member = new Member(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getBirthday(),
                request.getGender()
        );
        // 추후 중복 Email 확인
        Long memberId = memberService.join(member);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 자기 정보 조회
     */
    @GetMapping("/members/{member_id}")
    public FindMemberResponse findMemberById(@PathVariable("member_id")Long memberId){
        Member member = memberService.findById(memberId);
        return new FindMemberResponse(member);
    }

    /**
     * 이메일 수정
     */
    @PatchMapping("/members/{member_id}/email")
    public ResponseEntity updateMemberEmail(@PathVariable("member_id")Long memberId,String email){
        memberService.updateEmail(memberId,email);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/members/{member_id}/password")
    public ResponseEntity updateMemberPassword(@PathVariable("member_id")Long memberId,String password){
        memberService.updatePassword(memberId,password);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 닉네임 수정
     */
    @PatchMapping("/members/{member_id}/nickname")
    public ResponseEntity updateMemberNickname(@PathVariable("member_id")Long memberId,String nickname){
        memberService.updateNickname(memberId,nickname);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/members/{member_id}")
    public ResponseEntity removeMember(@PathVariable("member_id")Long memberId){
        memberService.removeMember(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
