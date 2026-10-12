package com.bryan.mantenimiento.config;

import com.bryan.mantenimiento.entity.Usuario;
import com.bryan.mantenimiento.entity.enums.Rol;
import com.bryan.mantenimiento.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            repo.save(new Usuario("admin", encoder.encode("Admin123!"),
                    "Administrador", Rol.ADMIN));
            repo.save(new Usuario("tecnico", encoder.encode("Tecnico123!"),
                    "Juan Pérez", Rol.TECNICO));
        }
    }
}
