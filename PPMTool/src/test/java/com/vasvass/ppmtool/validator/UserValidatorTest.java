package com.vasvass.ppmtool.validator;

import com.vasvass.ppmtool.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.*;

class UserValidatorTest {

    private UserValidator validator;
    private User user;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();

        user = new User();
        user.setUsername("user@example.com");
        user.setFullName("Test User");
        user.setPassword("secret123");
        user.setConfirmPassword("secret123");
    }

    @Test
    void supports_userClass_returnsTrue() {
        assertThat(validator.supports(User.class)).isTrue();
    }

    @Test
    void supports_otherClass_returnsFalse() {
        assertThat(validator.supports(Object.class)).isFalse();
    }

    @Test
    void validate_validUser_noErrors() {
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    void validate_shortPassword_addsPasswordError() {
        user.setPassword("abc");
        user.setConfirmPassword("abc");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertThat(errors.hasFieldErrors("password")).isTrue();
        assertThat(errors.getFieldError("password").getDefaultMessage())
                .isEqualTo("Password must be at least 6 characters");
    }

    @Test
    void validate_nullPassword_addsPasswordError() {
        user.setPassword(null);
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertThat(errors.hasFieldErrors("password")).isTrue();
    }

    @Test
    void validate_passwordMismatch_addsConfirmPasswordError() {
        user.setPassword("secret123");
        user.setConfirmPassword("different");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertThat(errors.hasFieldErrors("confirmPassword")).isTrue();
        assertThat(errors.getFieldError("confirmPassword").getDefaultMessage())
                .isEqualTo("Passwords must match");
    }

    @Test
    void validate_shortPasswordAndMismatch_addsBothErrors() {
        user.setPassword("abc");
        user.setConfirmPassword("xyz");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertThat(errors.hasFieldErrors("password")).isTrue();
        assertThat(errors.hasFieldErrors("confirmPassword")).isTrue();
    }

    @Test
    void validate_nullPasswordDoesNotTriggerMismatchError() {
        user.setPassword(null);
        user.setConfirmPassword("something");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        // Only password length error, no confirmPassword error (null check guard)
        assertThat(errors.hasFieldErrors("password")).isTrue();
        assertThat(errors.hasFieldErrors("confirmPassword")).isFalse();
    }
}
