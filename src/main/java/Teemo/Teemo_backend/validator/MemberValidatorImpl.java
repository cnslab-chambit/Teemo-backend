package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Role;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class MemberValidatorImpl implements MemberValidator{
    static final int MaxEmailLength = 100;
    static final int MinEmailLength = 5;
    static final int PasswordLength = 64;
    static final int MaxNicknameLength = 10;
    static final int MinNicknameLength = 2;

    @Override
    public boolean checkNicknameLength(String nickname){
        return MinNicknameLength<=nickname.length()&&nickname.length()<=MaxNicknameLength;
    }
    @Override
    public boolean checkEmailLength(String email) {
        return MinEmailLength <= email.length() && email.length() <= MaxEmailLength;
    }
    @Override
    public boolean checkPasswordLength(String password) {
        return password.length() == PasswordLength;
    }
    @Override
    public boolean checkBirthdayAssertion(String birthday){
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(100);
        LocalDate date = LocalDate.parse(birthday);
        return date.isAfter(minDate) && date.isBefore(today);

    }
    @Override
    public boolean checkBirthdayExpression(String birthday) {
        return birthday.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    @Override
    public boolean comparePassword(String password, String shouldBe){
        return password.equals(shouldBe);
    }

    @Override
    public boolean checkDeadLine(LocalDate now, LocalDate deletedAt)  {
        return now.isBefore(deletedAt);
    }

    @Override
    public boolean checkRole(Role found, Role shouldBe) {
        return (found == shouldBe);
    }
}
