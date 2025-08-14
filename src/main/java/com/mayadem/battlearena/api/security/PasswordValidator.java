package com.mayadem.battlearena.api.security;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    // Regex explanation:
    // ^ → start of string
    // (?=.*[A-Z].*[A-Z]) → at least 2 uppercase letters
    // (?=.*[a-z].*[a-z].*[a-z]) → at least 3 lowercase letters
    // (?=.*[0-9].*[0-9]) → at least 2 digits
    // (?=.*[!@#$&*]) → at least 1 special char from !@#$&*
    // .{8,} → at least 8 characters in total
    // $ → end of string
    @SuppressWarnings({ "java:S5998", "java:S5843" })
    private static final String VALIDATOR_PATTERN = "^(?=(?:.*[A-Z]){2,})(?=(?:.*[a-z]){3,})(?=(?:.*\\d){2,})(?=.*[!@#$&*]).{8,}$";

    private PasswordValidator() {
        // Private constructor to prevent instantiation
    }

    public static List<String> validate(String newPassword, String currentPassword) {
        List<String> errors = new ArrayList<>();

        if (newPassword == null || newPassword.isEmpty()) {
            errors.add("Password cannot be empty.");
            return errors;
        }

        if (newPassword.equals(currentPassword)) {
            errors.add("New password cannot be the same as the current password.");
        }

        if (!newPassword.matches(VALIDATOR_PATTERN)) {
            if (newPassword.length() < 8) {
                errors.add("Password must be at least 8 characters long.");
            }
            if (newPassword.replaceAll("[^A-Z]", "").length() < 2) {
                errors.add("Password must contain at least 2 uppercase letters.");
            }
            if (newPassword.replaceAll("[^a-z]", "").length() < 3) {
                errors.add("Password must contain at least 3 lowercase letters.");
            }
            if (newPassword.replaceAll("[^0-9]", "").length() < 2) {
                errors.add("Password must contain at least 2 digits.");
            }
            if (!newPassword.matches(".*[!@#$&*].*")) {
                errors.add("Password must contain at least one special character (!@#$&*).");
            }
        }

        return errors;
    }
}
