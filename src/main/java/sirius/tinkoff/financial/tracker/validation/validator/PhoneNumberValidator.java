package sirius.tinkoff.financial.tracker.validation.validator;

import sirius.tinkoff.financial.tracker.validation.annotation.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, Long> {
    @Override
    public boolean isValid(Long phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber != null && phoneNumber >= 80000000000L && phoneNumber <= 89999999999L;
    }
}
