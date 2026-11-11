package com.axiomdevv.chatapp.identity.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestTest {

    private static final String VALID_USERNAME = "naystar";
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
        LoginRequest request = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsBlankUsername() {
        LoginRequest request = new LoginRequest("", VALID_PASSWORD);

        assertTrue(hasViolationOn(request, "username"));
    }

    @Test
    void rejectsBlankPassword() {
        LoginRequest request = new LoginRequest(VALID_USERNAME, "");

        assertTrue(hasViolationOn(request, "password"));
    }

    @Test
    void acceptsShortPassword() {
        // Login has no minimum length on purpose: a future policy change
        // must never stop old passwords from logging in.
        LoginRequest request = new LoginRequest(VALID_USERNAME, "abc");

        assertFalse(hasViolationOn(request, "password"));
    }

    @Test
    void rejectsPasswordLongerThan72Characters() {
        LoginRequest request = new LoginRequest(VALID_USERNAME, "a".repeat(73));

        assertTrue(hasViolationOn(request, "password"));
    }

    @Test
    void toStringDoesNotExposePassword() {
        LoginRequest request = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);

        assertFalse(request.toString().contains(VALID_PASSWORD));
    }

    /** True if validation reports at least one problem on the given field. */
    private static boolean hasViolationOn(Object request, String field) {
        return validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals(field));
    }
}