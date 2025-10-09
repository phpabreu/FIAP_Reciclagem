package br.com.fiap.Reciclagem.repository;

import br.com.fiap.Reciclagem.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontoRepository extends JpaRepository<PontoColeta, Long> {

}