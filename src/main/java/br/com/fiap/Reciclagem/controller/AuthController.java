package br.com.fiap.Reciclagem.controller;

import br.com.fiap.Reciclagem.config.security.TokenService;
import br.com.fiap.Reciclagem.dto.LoginDTO;
import br.com.fiap.Reciclagem.dto.TokenDTO;
import br.com.fiap.Reciclagem.dto.UserDTO;
import br.com.fiap.Reciclagem.model.PontoColeta;
import br.com.fiap.Reciclagem.model.User;
import br.com.fiap.Reciclagem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TokenService tokenService;

    @GetMapping("/allUsers")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }


        @PostMapping("/register")
        public ResponseEntity registerUser(@RequestBody User user) {
            String cryptoPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(cryptoPassword);

            User newUser = userRepository.save(user);
            return ResponseEntity.created(null).body(newUser);
        }

        @PostMapping("/login")
        public ResponseEntity login(@RequestBody LoginDTO loginDTO) {

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(), loginDTO.password()
                    );

            Authentication authentication = authenticationManager.authenticate(auth);

            String token = tokenService.generateToken((User) authentication.getPrincipal());

            return ResponseEntity.ok(new TokenDTO(token));

        }

}

