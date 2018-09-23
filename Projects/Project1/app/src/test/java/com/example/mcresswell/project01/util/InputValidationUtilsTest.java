package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidationUtilsTest {

    @Test
    public void inputIsNullOrEmpty() {
        assertFalse(InputValidationUtils.validateNotNullOrEmpty(null));
        assertFalse(InputValidationUtils.validateNotNullOrEmpty("     \n"));
        assertFalse(InputValidationUtils.validateNotNullOrEmpty(""));
        assertFalse(InputValidationUtils.validateNotNullOrEmpty(" "));

    }

    @Test
    public void inputIsNotNullOrEmpty() {
        assertTrue(InputValidationUtils.validateNotNullOrEmpty("TEST"));
        assertTrue(InputValidationUtils.validateNotNullOrEmpty("     \nTEST"));
        assertTrue(InputValidationUtils.validateNotNullOrEmpty("  TEST  "));
        assertTrue(InputValidationUtils.validateNotNullOrEmpty(" TEST SPACES "));

    }

    @Test
    public void validateEmail_validEmailInput() {
        assertTrue(InputValidationUtils.validateEmail("test123@test.com"));
        assertTrue(InputValidationUtils.validateEmail("test_123@test.com"));
        assertTrue(InputValidationUtils.validateEmail("test-123@test.com"));
        assertTrue(InputValidationUtils.validateEmail("test.test@test.com"));
        assertTrue(InputValidationUtils.validateEmail("test@hotmail.co.uk"));
        assertTrue(InputValidationUtils.validateEmail("123456789@test.com"));
        assertTrue(InputValidationUtils.validateEmail("   123456789@test.com   "));



    }

    @Test
    public void validateEmail_invalidEmailInput() {
        assertFalse(InputValidationUtils.validateEmail("email"));
        assertFalse(InputValidationUtils.validateEmail("emailwith @ spaces.com"));
        assertFalse(InputValidationUtils.validateEmail("helloemail@test"));
        assertFalse(InputValidationUtils.validateEmail("hello*email@test.com"));
        assertFalse(InputValidationUtils.validateEmail("hello#email@test.com"));

    }

}