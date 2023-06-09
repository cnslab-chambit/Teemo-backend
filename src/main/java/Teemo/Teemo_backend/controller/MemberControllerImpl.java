package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.dtos.*;
import Teemo.Teemo_backend.error.CustomErrorResponse;
import Teemo.Teemo_backend.error.CustomInvalidValueException;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberControllerImpl implements MemberController{
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 회원 생성
     */
    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signup(@RequestBody MemberSignupRequest request)
    {
        log.info("회원 생성");
        try {
            memberService.join(request);
        }
        catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.info("정상 응답");
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 자기 정보 조회
     */
    @GetMapping("/find")
    public ResponseEntity<MemberFindResponse> findMember(@RequestParam Long memberId){
        log.info("자기 정보 조회");
        Member member = null;
        try{
            member = memberService.find(memberId);
        }
        catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        MemberFindResponse response = new MemberFindResponse(
                member.getEmail(),
                member.getNickname(),
                member.getBirthday(),
                member.getGender(),
                member.getCreatedAt(),
                member.getDeletedAt()
        );
        log.info("정상 응답");
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity updateMember(@RequestBody MemberUpdateRequest request){
        log.info("회원 정보 수정");
        try {
            memberService.update(request);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.info("정상 응답");
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdrawal/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId){
        log.info("회원 탈퇴");
        try {
            memberService.remove(memberId);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }log.info("정상 응답");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 회원 접속 (로그인)
     */
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request){
        log.info("회원 접속");
        Member member = null;
        try{
            member = memberService.login(request);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        MemberLoginResponse response = new MemberLoginResponse(member.getId(),member.getRole());
        log.info("정상 응답");
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 접속해제 (로그아웃)
     */
    @GetMapping("/logout/{memberId}")
    public ResponseEntity logout(@PathVariable Long memberId){
        log.info("회원 접속해제");
        try {
            memberService.logout(memberId);
        }catch (CustomInvalidValueException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(e.getField(), e.getMessage());
            log.info("비정상 응답");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
        log.info("정상 응답");
        return new ResponseEntity(HttpStatus.OK);
    }

}
