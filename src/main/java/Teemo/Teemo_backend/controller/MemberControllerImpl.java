package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.dtos.*;
import Teemo.Teemo_backend.error.CustomErrorResponse;
import Teemo.Teemo_backend.error.CustomInvalidValueException;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberControllerImpl implements MemberController{
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
        catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 자기 정보 조회
     */
    @GetMapping("/find")
    public ResponseEntity<MemberFindResponse> findMember(@RequestParam Long memberId){
        Member member = null;
        try{
            member = memberService.find(memberId);
        }
        catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        MemberFindResponse response = new MemberFindResponse(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getBirthday(),
                member.getGender(),
                member.getCreatedAt(),
                member.getDeletedAt()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity updateMember(@RequestBody MemberUpdateRequest request){
        try {
            memberService.update(request);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdrawal/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId){
        try {
            memberService.remove(memberId);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 접속 (로그인)
     */
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(MemberLoginRequest request){
        Member member = null;
        try{
            member = memberService.login(request);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        MemberLoginResponse response = new MemberLoginResponse(member.getId(),member.getRole());
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 접속해제 (로그아웃)
     */
    @GetMapping("/logout/{memberId}")
    public ResponseEntity logout(@PathVariable Long memberId){
        try {
            memberService.logout(memberId);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
