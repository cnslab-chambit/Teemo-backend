package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.dtos.MemberFindResponse;
import Teemo.Teemo_backend.domain.dtos.MemberSignupRequest;
import Teemo.Teemo_backend.domain.dtos.MemberUpdateRequest;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberControllerImpl {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 회원 생성
     */
    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signup(@RequestBody MemberSignupRequest request)
    {   try {
            memberService.join(request);
        }
        catch (Exception e) {}
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 자기 정보 조회
     */
    @GetMapping("/find")
    public MemberFindResponse findMember(@RequestParam Long memberId){
        Member member = null;
        try{
            member = memberService.find(memberId);
        }
        catch (Exception e){}


        MemberFindResponse response = new MemberFindResponse(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getBirthday(),
                member.getGender(),
                member.getCreatedAt(),
                member.getDeletedAt()
        );
        return response;
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity updateMember(@RequestBody MemberUpdateRequest request){
        try {
            memberService.update(request);
        }catch (Exception e){}
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdrawal/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId){
        try {
            memberService.remove(memberId);
        }catch(Exception e){}
        return new ResponseEntity(HttpStatus.OK);
    }

}
