package Teemo.Teemo_backend.validator;

import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.Role;

public class MemberValidatorImpl implements MemberValidator{
    @Override
    public boolean found(Object found) {
        if(found!=null) return true;
        return false;
    }
    @Override
    public boolean checkRole(Role found, Role shouldBe) {
        if(found == shouldBe) return true;
        return false;
    }
}
