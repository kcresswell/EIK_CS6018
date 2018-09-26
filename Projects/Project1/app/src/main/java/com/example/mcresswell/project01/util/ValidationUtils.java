package com.example.mcresswell.project01.util;

//TODO: needs tests
public class ValidationUtils {

    public static boolean isValidCity(String city, String countryCode) {
        return ValidationUtils.isNotNullOrEmpty(city);
    }

    public static boolean isValidCountryCode(String countryCode) {
        return ValidationUtils.isNotNullOrEmpty(countryCode) &&
                countryCode.trim().length() == 2;
    }

    public static boolean isValidEmail(String input) {
        return isNotNullOrEmpty(input) &&
                input.trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean isValidAlphaChars(String input) {
        return isNotNullOrEmpty(input) &&
                input.trim().matches("[A-Za-z]+");
    }


    public static boolean isValidDobFormat(String input) {
        return isNotNullOrEmpty(input) &&
                input.trim().matches("[0-9]{2}/[0-9]{2}/[0-9]{4}");
    }

    public static boolean isValidWeight(String input) {
        return isNotNullOrEmpty(input) &&
                input.trim().matches("[0-9]{1,3}\\.?[0-9]?");
    }

    public static boolean isValidHeight(String feet, String inches) {
        return isNotNullOrEmpty(feet) && isNotNullOrEmpty(inches)
                && feet.trim().matches("[0-9]{1,2}") &&
                inches.trim().matches("[0-9]{1,2}");
    }

    public static boolean isValidAlphaNumericChars(String input) {
        return isNotNullOrEmpty(input) &&
                input.trim().matches("[A-Za-z0-9]+");
    }

    public static boolean isNotNullOrEmpty(String input) {
        return input != null && !input.isEmpty() && !input.matches("^\\s+$");
    }
}
