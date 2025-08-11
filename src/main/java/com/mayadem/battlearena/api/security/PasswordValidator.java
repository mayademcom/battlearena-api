package com.mayadem.battlearena.api.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordValidator {

    // Precompiled patterns (safe, no heavy backtracking)
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]");

    public static List<String> validate(String newPassword, String currentPassword) {
        List<String> errors = new ArrayList<>();

        if (newPassword == null || newPassword.isEmpty()) {
            errors.add("Şifre boş olamaz.");
            return errors;
        }

        // Mevcut şifreyle aynı mı?
        if (newPassword.equals(currentPassword)) {
            errors.add("Yeni şifre, mevcut şifreyle aynı olamaz.");
        }

        // Minimum uzunluk kontrolü
        if (newPassword.length() < 8) {
            errors.add("Şifre en az 8 karakter olmalıdır.");
        }

        // Büyük harf kontrolü
        if (!UPPERCASE_PATTERN.matcher(newPassword).find()) {
            errors.add("Şifre en az bir büyük harf içermelidir.");
        }

        // Küçük harf kontrolü
        if (!LOWERCASE_PATTERN.matcher(newPassword).find()) {
            errors.add("Şifre en az bir küçük harf içermelidir.");
        }

        // Rakam kontrolü
        if (!DIGIT_PATTERN.matcher(newPassword).find()) {
            errors.add("Şifre en az bir rakam içermelidir.");
        }

        // Özel karakter kontrolü
        if (!SPECIAL_CHAR_PATTERN.matcher(newPassword).find()) {
            errors.add("Şifre en az bir özel karakter içermelidir.");
        }

        return errors;
    }
}
