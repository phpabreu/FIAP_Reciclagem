package br.com.fiap.Reciclagem.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private VerifyToken verifyToken;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf ->
                        csrf.disable()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/auth/allUsers").permitAll()

                                .requestMatchers(HttpMethod.GET, "/recipiente/*").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/recipiente").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/recipiente/*").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/recipiente/register").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/recipiente").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/pontoColeta/*").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/pontoColeta").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/pontoColeta/*").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/pontoColeta/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/pontoColeta").hasRole("ADMIN")


                                .requestMatchers(HttpMethod.GET, "/material/*").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/material").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/material/*").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/material/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/material").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/alerta/*").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/alerta").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/alerta/*").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/alerta/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/alerta").hasRole("ADMIN")

                                .anyRequest().authenticated()
                )
                .addFilterBefore(
                        verifyToken,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
