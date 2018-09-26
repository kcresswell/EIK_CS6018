package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationUtilsTest {

    @Test
    public void inputIsNullOrEmpty() {
        assertFalse(ValidationUtils.isNotNullOrEmpty(null));
        assertFalse(ValidationUtils.isNotNullOrEmpty("     \n"));
        assertFalse(ValidationUtils.isNotNullOrEmpty(""));
        assertFalse(ValidationUtils.isNotNullOrEmpty(" "));

    }

    @Test
    public void inputIsNotNullOrEmpty() {
        assertTrue(ValidationUtils.isNotNullOrEmpty("TEST"));
        assertTrue(ValidationUtils.isNotNullOrEmpty("     \nTEST"));
        assertTrue(ValidationUtils.isNotNullOrEmpty("  TEST  "));
        assertTrue(ValidationUtils.isNotNullOrEmpty(" TEST SPACES "));

    }

    @Test
    public void validateEmail_validEmailInput() {
        assertTrue(ValidationUtils.isValidEmail("test123@test.com"));
        assertTrue(ValidationUtils.isValidEmail("test_123@test.com"));
        assertTrue(ValidationUtils.isValidEmail("test-123@test.com"));
        assertTrue(ValidationUtils.isValidEmail("test.test@test.com"));
        assertTrue(ValidationUtils.isValidEmail("test@hotmail.co.uk"));
        assertTrue(ValidationUtils.isValidEmail("123456789@test.com"));
        assertTrue(ValidationUtils.isValidEmail("   123456789@test.com   "));



    }

    @Test
    public void validateEmail_invalidEmailInput() {
        assertFalse(ValidationUtils.isValidEmail("email"));
        assertFalse(ValidationUtils.isValidEmail("emailwith @ spaces.com"));
        assertFalse(ValidationUtils.isValidEmail("helloemail@test"));
        assertFalse(ValidationUtils.isValidEmail("hello*email@test.com"));
        assertFalse(ValidationUtils.isValidEmail("hello#email@test.com"));

    }
}