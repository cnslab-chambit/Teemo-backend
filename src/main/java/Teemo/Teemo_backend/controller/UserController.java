package Teemo.Teemo_backend.controller;

import Teemo.Teemo_backend.domain.User;
import Teemo.Teemo_backend.domain.dtos.UserFindResponse;
import Teemo.Teemo_backend.domain.dtos.UserSignupRequest;
import Teemo.Teemo_backend.domain.dtos.UserUpdateRequest;
import Teemo.Teemo_backend.repository.UserRepository;
import Teemo.Teemo_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 회원 생성
     */
    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signup(@RequestBody UserSignupRequest request)
    {   try {
            userService.join(request);
        }
        catch (Exception e) {}
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 자기 정보 조회
     */
    @GetMapping("/find/{userId}")
    public UserFindResponse findUser(@PathVariable Long userId){
        User user = null;
        try{
            user = userService.find(userId);
        }
        catch (Exception e){}


        UserFindResponse response = new UserFindResponse(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getBirthday(),
                user.getGender(),
                user.getCreatedAt(),
                user.getDeletedAt()
        );
        return response;
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestBody UserUpdateRequest request){
        try {
            userService.update(request);
        }catch (Exception e){}
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdrawal/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId){
        try {
            userService.remove(userId);
        }catch(Exception e){}
        return new ResponseEntity(HttpStatus.OK);
    }

}
