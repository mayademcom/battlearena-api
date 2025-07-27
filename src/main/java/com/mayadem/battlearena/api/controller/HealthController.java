package com.mayadem.battlearena.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Bu sınıftaki tüm endpoint'lerin başına /api gelecek.
public class HealthController {

    /**
     * API'nin ayakta ve çalışır durumda olup olmadığını kontrol eden endpoint.
     * @return API'nin çalıştığını belirten basit bir mesaj.
     */
    @GetMapping("/health") // http://localhost:8080/api/health adresine gelen GET isteklerini karşılar.
    public String healthCheck() {
        return "API is up and running!";
    }
}
