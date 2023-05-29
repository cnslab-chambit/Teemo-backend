package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;

import java.time.LocalDate;

public interface MemberValidator {
    public boolean comparePassword(String password, String shouldBe);
    boolean checkDeadLine(LocalDate now, LocalDate deletedAt);
    public boolean checkRole(Role found, Role shouldBe);
}
