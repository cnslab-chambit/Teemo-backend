package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MemberValidatorImpl implements MemberValidator{
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
