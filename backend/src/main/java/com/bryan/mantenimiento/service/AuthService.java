package com.bryan.mantenimiento.service;

import com.bryan.mantenimiento.dto.LoginRequest;
import com.bryan.mantenimiento.dto.LoginResponse;
import com.bryan.mantenimiento.dto.UsuarioResponse;
import com.bryan.mantenimiento.entity.Usuario;
import com.bryan.mantenimiento.exception.RecursoNoEncontradoException;
import com.bryan.mantenimiento.mapper.EntityMapper;
import com.bryan.mantenimiento.repository.UsuarioRepository;
import com.bryan.mantenimiento.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepo;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager,
                       UsuarioRepository usuarioRepo,
                       JwtService jwtService) {
        this.authManager = authManager;
        this.usuarioRepo = usuarioRepo;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest req) {
        // Lanza BadCredentialsException si el usuario o la contraseña no coinciden
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        Usuario u = usuarioRepo.findByUsername(req.username()).orElseThrow();
        String token = jwtService.generarToken(u.getUsername(), u.getRol().name());
        return new LoginResponse(token, "Bearer", jwtService.getExpirationMs(),
                u.getUsername(), u.getNombre(), u.getRol());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse perfil(String username) {
        Usuario u = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return EntityMapper.toResponse(u);
    }
}
