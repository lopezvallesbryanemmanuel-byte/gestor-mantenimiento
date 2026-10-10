package com.bryan.mantenimiento.security;

import com.bryan.mantenimiento.entity.Usuario;
import com.bryan.mantenimiento.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRol().name()) // Spring lo guarda como "ROLE_ADMIN" o "ROLE_TECNICO"
                .disabled(!u.getActivo())
                .build();
    }
}
