package com.bryan.mantenimiento.controller;

import com.bryan.mantenimiento.dto.LoginRequest;
import com.bryan.mantenimiento.dto.LoginResponse;
import com.bryan.mantenimiento.dto.UsuarioResponse;
import com.bryan.mantenimiento.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication auth) { // auth.getName() = username del token
        return authService.perfil(auth.getName());
    }
}
