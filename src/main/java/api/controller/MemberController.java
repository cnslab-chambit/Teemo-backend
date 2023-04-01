package api.controller;

import api.domain.Member;
import api.domain.Role;
import api.domain.dtos.CreateMemberRequest;
import api.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원 생성
     */
    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody CreateMemberRequest request){
        Member member = new Member(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                request.getBirthday(),
                request.getRole(),
                request.getGender()
        );
        // 추후 중복 Email 확인
        Long memberId = memberService.join(member);
        return new ResponseEntity(HttpStatus.CREATED);
    }


}
