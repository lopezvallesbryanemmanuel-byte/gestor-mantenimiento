package com.bryan.mantenimiento.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    // Secreto aleatorio de 256 bits en Base64, generado solo para la prueba
    private static final String SECRETO =
            Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded());

    private final UserDetails admin = User.withUsername("admin")
            .password("x").roles("ADMIN").build();

    @Test
    void generaYLeeUnToken() {
        JwtService service = new JwtService(SECRETO, 60_000);

        String token = service.generarToken("admin", "ADMIN");

        assertEquals("admin", service.extraerUsername(token));
        assertTrue(service.esValido(token, admin));
    }

    @Test
    void rechazaUnTokenAlterado() {
        JwtService service = new JwtService(SECRETO, 60_000);
        String token = service.generarToken("admin", "ADMIN");

        String alterado = token.substring(0, token.length() - 2) + "xx";

        assertThrows(JwtException.class, () -> service.extraerUsername(alterado));
    }

    @Test
    void rechazaUnTokenVencido() {
        JwtService service = new JwtService(SECRETO, -1_000);
        String token = service.generarToken("admin", "ADMIN");

        assertThrows(ExpiredJwtException.class, () -> service.extraerUsername(token));
    }
}
