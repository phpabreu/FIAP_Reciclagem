package br.com.fiap.Reciclagem.config.security;

import br.com.fiap.Reciclagem.model.User;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${secret.word}")
    private String secret;

    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer("reciclagem")
                .withSubject(user.getEmail())
                .withExpiresAt(expirationTime())
                .sign(algorithm);

        return token;

    }

    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("reciclagem")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }

    }

    private Instant expirationTime(){

        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));

    }

}

