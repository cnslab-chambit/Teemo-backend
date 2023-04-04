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
    @GetMapping("/members/{memberId}")
    public FindMemberResponse findMemberById(@PathVariable("memberId")Long memberId){
        Member member = memberService.findById(memberId);
        return new FindMemberResponse(member);
    }
    /**
     * 이메일 수정
     */
    @PatchMapping("/members/{memberId}/email")
    public ResponseEntity updateMemberEmail(
            @PathVariable("memberId")Long memberId,
            @RequestParam("email")String email){
        memberService.updateEmail(memberId,email);
        return new ResponseEntity(HttpStatus.OK);
    }
    /**
     * 비밀번호 수정
     */
    @PatchMapping("/members/{memberId}/password")
    public ResponseEntity updateMemberPassword(@PathVariable("memberId")Long memberId,
                                               @RequestParam("password")String password){
        memberService.updatePassword(memberId,password);
        return new ResponseEntity(HttpStatus.OK);
    }
    /**
     * 닉네임 수정
     */
    @PatchMapping("/members/{memberId}/nickname")
    public ResponseEntity updateMemberNickname(@PathVariable("memberId")Long memberId,
                                               @RequestParam("nickname")String nickname){
        memberService.updateNickname(memberId,nickname);
        return new ResponseEntity(HttpStatus.OK);
    }
    /**
     * 회원탈퇴
     */
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity removeMember(@PathVariable("memberId")Long memberId){
        memberService.removeMember(memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
