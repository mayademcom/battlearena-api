package com.mayadem.battlearena.api.security;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    public static List<String> validate(String newPassword, String currentPassword) {
    List<String> errors = new ArrayList<>();

    // Check if password is null or empty
    if (newPassword == null || newPassword.isEmpty()) {
        errors.add("Password cannot be empty.");
        return errors;
    }

    // Check if new password is the same as current password
    if (newPassword.equals(currentPassword)) {
        errors.add("New password cannot be the same as the current password.");
    }

    // Minimum length check
    if (newPassword.length() < 8) {
        errors.add("Password must be at least 8 characters long.");
    }

    // Flags to track different password requirements
    boolean hasUpper = false;   // At least one uppercase letter
    boolean hasLower = false;   // At least one lowercase letter
    boolean hasDigit = false;   // At least one digit
    boolean hasSpecial = false; // At least one special character

    // Loop through each character once and update flags
    for (char ch : newPassword.toCharArray()) {
        if (Character.isUpperCase(ch)) {
            hasUpper = true;
        } else if (Character.isLowerCase(ch)) {
            hasLower = true;
        } else if (Character.isDigit(ch)) {
            hasDigit = true;
        } else {
            hasSpecial = true; // Any non-alphanumeric character
        }
    }

    // Add error messages for missing requirements
    if (!hasUpper) {
        errors.add("Password must contain at least one uppercase letter.");
    }
    if (!hasLower) {
        errors.add("Password must contain at least one lowercase letter.");
    }
    if (!hasDigit) {
        errors.add("Password must contain at least one digit.");
    }
    if (!hasSpecial) {
        errors.add("Password must contain at least one special character.");
    }

    return errors;
}

}
