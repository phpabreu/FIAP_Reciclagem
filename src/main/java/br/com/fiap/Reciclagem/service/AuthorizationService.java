package br.com.fiap.Reciclagem.service;

import br.com.fiap.Reciclagem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tenta encontrar o usuário
        UserDetails user = userRepository.findByEmail(username);

        // 2. Verifica se o usuário é nulo. Se for, lança a exceção exigida pela interface.
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        // 3. Se encontrado, retorna o objeto UserDetails
        return user;
    }
}