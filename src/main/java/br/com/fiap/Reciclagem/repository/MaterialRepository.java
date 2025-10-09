package br.com.fiap.Reciclagem.repository;

import br.com.fiap.Reciclagem.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
