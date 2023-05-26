package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class MemberValidatorImpl implements MemberValidator{
    @Override
    public boolean checkRole(Role found, Role shouldBe) {
        if(found == shouldBe) return true;
        return false;
    }
}
