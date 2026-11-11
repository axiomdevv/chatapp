package com.axiomdevv.chatapp.identity.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestTest {

    private static final String VALID_USERNAME = "naystar";
    private static final String VALID_EMAIL = "naystar@example.com";
    private static final String VALID_PASSWORD = "secret123";

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    @Test
    void validRequestHasNoViolations() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        assertTrue(validator.validate(request).isEmpty());
    }

    // username

    @ParameterizedTest
    @ValueSource(strings = {"abc", "naystar", "Ali_99", "N_a_y_s_t_a_r"})
    void acceptsValidUsernames(String username) {
        RegisterRequest request = new RegisterRequest(username, VALID_EMAIL, VALID_PASSWORD);

        assertFalse(hasViolationOn(request, "username"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "_naystar",   // starts with underscore
            "9lives",     // starts with a digit
            "nay@star",   // contains @
            "nay.star",   // contains a dot
            "nay star",   // contains a space
            "na",         // too short (2 characters)
            ""            // blank
    })
    void rejectsInvalidUsernames(String username) {
        RegisterRequest request = new RegisterRequest(username, VALID_EMAIL, VALID_PASSWORD);

        assertTrue(hasViolationOn(request, "username"));
    }

    @Test
    void acceptsUsernameOfExactly50Characters() {
        RegisterRequest request = new RegisterRequest("a".repeat(50), VALID_EMAIL, VALID_PASSWORD);

        assertFalse(hasViolationOn(request, "username"));
    }

    @Test
    void rejectsUsernameLongerThan50Characters() {
        RegisterRequest request = new RegisterRequest("a".repeat(51), VALID_EMAIL, VALID_PASSWORD);

        assertTrue(hasViolationOn(request, "username"));
    }

    // email

    @Test
    void rejectsInvalidEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, "not-an-email", VALID_PASSWORD);

        assertTrue(hasViolationOn(request, "email"));
    }

    @Test
    void rejectsBlankEmail() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, "", VALID_PASSWORD);

        assertTrue(hasViolationOn(request, "email"));
    }

    // ---------- password ----------

    @Test
    void rejectsPasswordShorterThan8Characters() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, "short12");

        assertTrue(hasViolationOn(request, "password"));
    }

    @Test
    void acceptsPasswordOfExactly8Characters() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, "abcd1234");

        assertFalse(hasViolationOn(request, "password"));
    }

    @Test
    void rejectsPasswordLongerThan72Characters() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, "a".repeat(73));

        assertTrue(hasViolationOn(request, "password"));
    }

    // toString

    @Test
    void toStringDoesNotExposePassword() {
        RegisterRequest request = new RegisterRequest(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        assertFalse(request.toString().contains(VALID_PASSWORD));
    }



    /** True if validation reports at least one problem on the given field. */
    private static boolean hasViolationOn(Object request, String field) {
        return validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals(field));
    }
}