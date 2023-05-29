package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.dtos.*;
import org.springframework.http.ResponseEntity;

public interface MemberController {
    public ResponseEntity signup(MemberSignupRequest request);
    public ResponseEntity<MemberFindResponse> findMember(Long memberId);
    public ResponseEntity updateMember(MemberUpdateRequest request);
    public ResponseEntity deleteMember(Long memberId);
    public ResponseEntity<MemberLoginResponse> login(MemberLoginRequest request);
    public ResponseEntity logout(Long memberId);
}
