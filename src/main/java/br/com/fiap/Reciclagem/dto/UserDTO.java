package br.com.fiap.Reciclagem.dto;
import br.com.fiap.Reciclagem.model.User;

import br.com.fiap.Reciclagem.model.User;

public record UserDTO(
        Long usuarioId,
        String nome,
        String email,
        String role
) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
