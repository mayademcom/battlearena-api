package com.mayadem.battlearena.api.security;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
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
        if (!newPassword.matches(".*[A-Z].*")) {
            errors.add("Şifre en az bir büyük harf içermelidir.");
        }

        // Küçük harf kontrolü
        if (!newPassword.matches(".*[a-z].*")) {
            errors.add("Şifre en az bir küçük harf içermelidir.");
        }

        // Rakam kontrolü
        if (!newPassword.matches(".*\\d.*")) {
            errors.add("Şifre en az bir rakam içermelidir.");
        }

        // Özel karakter kontrolü
        if (!newPassword.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*")) {
            errors.add("Şifre en az bir özel karakter içermelidir.");
        }

        return errors;
    
}
}
