package Teemo.Teemo_backend.validator;

import org.springframework.stereotype.Component;

@Component
public class CommonValidatorImpl implements CommonValidator{
    @Override
    public boolean found(Object found) {
        if(found!=null) return true;
        return false;
    }
}
