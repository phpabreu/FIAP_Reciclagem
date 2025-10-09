package br.com.fiap.Reciclagem.service;

import br.com.fiap.Reciclagem.model.User;
import br.com.fiap.Reciclagem.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    // Mock: Cria um mock (simulação) do UserRepository.
    @Mock
    private UserRepository userRepository;

    // InjectMocks: Cria uma instância real do Service e injeta o mock acima nele.
    @InjectMocks
    private AuthorizationService authorizationService;

    // --- CENÁRIO 1: SUCESSO ---

    @Test
    @DisplayName("Deve retornar UserDetails quando o e-mail for encontrado no repositório")
    void loadUserByUsername_Sucesso() {
        // ARRANGE (Preparação)
        String email = "teste@exemplo.com";
        // Cria um objeto User simulado que o repositório deve retornar
        User userSimulado = new User();
        userSimulado.setEmail(email);

        // Define o comportamento do Mock (Simulação):
        // Quando userRepository.findByEmail for chamado com o 'email', ele deve retornar o 'userSimulado'.
        when(userRepository.findByEmail(email)).thenReturn(userSimulado);

        // ACT (Ação)
        UserDetails userDetails = authorizationService.loadUserByUsername(email);

        // ASSERT (Verificação)
        // 1. Verifica se o resultado não é nulo.
        assertNotNull(userDetails, "UserDetails não deve ser nulo.");
        // 2. Verifica se o nome de usuário (e-mail) retornado é o mesmo.
        assertEquals(email, userDetails.getUsername(), "O email do usuário deve corresponder ao que foi buscado.");
        // 3. Verifica se o metodo do repositório foi chamado *exatamente uma vez*.
        verify(userRepository, times(1)).findByEmail(email);
    }

    // --- CENÁRIO 2: FALHA ---

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o e-mail não for encontrado")
    void loadUserByUsername_Falha() {
        // ARRANGE (Preparação)
        String email = "naoexiste@exemplo.com";

        // Define o comportamento do Mock:
        // Quando userRepository.findByEmail for chamado, ele deve retornar null (usuário não encontrado).
        when(userRepository.findByEmail(email)).thenReturn(null);

        // ACT & ASSERT (Ação e Verificação combinadas)
        // Verifica se o metodo lança a exceção esperada (UsernameNotFoundException)
        assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername(email);
        }, "Deve lançar UsernameNotFoundException quando o usuário não é encontrado.");

        // Verifica se o metodo do repositório foi chamado.
        verify(userRepository, times(1)).findByEmail(email);
    }
}