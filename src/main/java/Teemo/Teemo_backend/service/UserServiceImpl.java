package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.User;
import Teemo.Teemo_backend.domain.dtos.UserFindResponse;
import Teemo.Teemo_backend.domain.dtos.UserSignupRequest;
import Teemo.Teemo_backend.domain.dtos.UserUpdateRequest;
import Teemo.Teemo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    @Transactional
    public void join(UserSignupRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();
        String birthday = request.getBirthday();
        Gender gender = request.getGender();

        /**
         * [제약조건]
         * 1. 빈 값 확인
         * 2. 이메일 중복 확인
         * 3. 닉네임 중복 확인
         * 4. 이메일 길이 조건 확인 : [5 ~ 100] 자 사이의 길이를 가져야 한다. (5자, 100자 포함)
         * 5. 비밀번호 길이 조건 확인 : [15] 자 길이를 가져야 한다.
         * 6. 닉네임 길이 조건 확인 : [2 ~ 10] 자 사이의 길이를 가져야 한다. (경계값 포함)
         * 7. 생년월일 조건 확인 : ( 금일 기준 100 년 전 ~ 금일 ) 사이의 값을 가져야 한다. (경계값 미포함)
         */
        User user = new User(email,password,nickname,birthday,gender);
        userRepository.save(user);
    }

    @Override
    public User find(Long id) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */

        User user = userRepository.findById(id);
        return user;
    }

    @Override
    public void update(UserUpdateRequest request) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */
        Long id = request.getId();
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        User user = userRepository.findById(id);
        user.updateAccountInfo(email,password,nickname);
    }

    @Override
    public void remove(Long id) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 아이디로 유저를 찾지 못한 경우
         */
        User user = userRepository.findById(id);
        userRepository.delete(user);
    }
}
