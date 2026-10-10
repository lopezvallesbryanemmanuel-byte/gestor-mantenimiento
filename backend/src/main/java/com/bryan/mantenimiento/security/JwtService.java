package com.bryan.mantenimiento.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    public String generarToken(String username, String rol) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expirationMs))
                .signWith(key) // HMAC-SHA256
                .compact();
    }

    public String extraerUsername(String token) {
        return parsear(token).getSubject();
    }

    public boolean esValido(String token, UserDetails user) {
        Claims claims = parsear(token);
        return claims.getSubject().equals(user.getUsername())
                && claims.getExpiration().after(new Date());
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    // Lanza JwtException si la firma es inválida, el token está mal formado o ya expiró
    private Claims parsear(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
    }
}
