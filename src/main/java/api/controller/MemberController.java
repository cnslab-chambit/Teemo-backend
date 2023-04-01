package api.controller;

import api.domain.Member;
import api.domain.Role;
import api.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * member 생성
     */
    @PostMapping("/member/add")
    public CreateMemberResponse createMember(@RequestBody CreateMemberRequest request){
        Member member = new Member(request.getNickname(),request.getRole());
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }


    @Data
    @AllArgsConstructor
    private static class CreateMemberResponse{
        private Long memberId;
    }
    @Data
    private static class CreateMemberRequest {
        private String nickname;
        private Role role;
    }
}
