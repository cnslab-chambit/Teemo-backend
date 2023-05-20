package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;

public interface MemberValidator {
    public boolean found(Object found);
    public boolean checkRole(Role found, Role shouldBe);
}
