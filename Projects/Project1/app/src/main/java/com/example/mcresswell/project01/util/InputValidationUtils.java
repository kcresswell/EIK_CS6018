package com.example.mcresswell.project01.util;

public class InputValidationUtils {

    public static boolean validateEmail(String input) {
        return validateNotNullOrEmpty(input) &&
                input.trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean validateAlphaCharacters(String input) {
        return validateNotNullOrEmpty(input) &&
                input.trim().matches("[A-Za-z]+");
    }


    public static boolean validateDateOfBirth(String input) {
        return validateNotNullOrEmpty(input) &&
                input.trim().matches("[0-9]{2}/[0-9]{2}/[0-9]{4}");
    }

    public static boolean validateWeight(String input) {
        return validateNotNullOrEmpty(input) &&
                input.trim().matches("[0-9]{1,3}\\.?[0-9]?");
    }

    public static boolean validateHeight(String feet, String inches) {
        return validateNotNullOrEmpty(feet) && validateNotNullOrEmpty(inches)
                && feet.trim().matches("[0-9]{1,2}") &&
                inches.trim().matches("[0-9]{1,2}");
    }

    public static boolean validateAlphaNumericCharacters(String input) {
        return validateNotNullOrEmpty(input) &&
                input.trim().matches("[A-Za-z0-9]+");
    }

    public static boolean validateNotNullOrEmpty(String input) {
        return input != null && !input.isEmpty() && !input.matches("^\\s+$");
    }
}
