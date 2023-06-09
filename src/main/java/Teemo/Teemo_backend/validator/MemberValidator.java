package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;

import java.time.LocalDate;

public interface MemberValidator {
    boolean checkEmailLength(String email);
    boolean checkPasswordLength(String password);
    boolean checkBirthdayAssertion(String birthday);
    boolean checkBirthdayExpression(String birthday);
    boolean checkNicknameLength(String nickname);
    boolean comparePassword(String password, String shouldBe);
    boolean checkDeadLine(LocalDate now, LocalDate deletedAt);
    boolean checkRole(Role found, Role shouldBe);
}
