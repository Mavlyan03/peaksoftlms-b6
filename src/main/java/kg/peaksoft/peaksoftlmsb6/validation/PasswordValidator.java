package kg.peaksoft.peaksoftlmsb6.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (password.length() > 5) {
            return password.matches("^[a-zA-Z0-9]{5,14}$");
        } else {
            return false;
        }
    }

}