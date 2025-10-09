package br.com.fiap.Reciclagem.controller;

import br.com.fiap.Reciclagem.model.PontoColeta;
import br.com.fiap.Reciclagem.repository.PontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pontoColeta")
public class PontoColetaController {

        @Autowired
        private PontoRepository pontoRepository;

        @GetMapping
        public List<PontoColeta> getPontoColeta() {
            return pontoRepository.findAll();
        }

        @PostMapping("/register")
        public ResponseEntity<PontoColeta> save(@RequestBody PontoColeta pontoColeta) {
            PontoColeta newPontoColeta = pontoRepository.save(pontoColeta);
            return ResponseEntity.created(null).body(newPontoColeta);
        }

        @GetMapping("/{id}")
        public ResponseEntity<PontoColeta> getById(@PathVariable Long id) {
            Optional<PontoColeta> pontoColeta = pontoRepository.findById(id);
            if (pontoColeta.isPresent()) {
                return ResponseEntity.ok(pontoColeta.get());
            }
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping("{id}")
        public ResponseEntity<PontoColeta> deleteById(@PathVariable Long id) {
            PontoColeta pontoColeta = pontoRepository.findById(id).orElse(null);
            if (pontoColeta != null) {
                pontoRepository.delete(pontoColeta);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }

        @PutMapping
        public ResponseEntity<PontoColeta> update(@RequestBody PontoColeta pontoColeta) {
            PontoColeta actualPontoColeta = pontoRepository.findById(pontoColeta.getIdPonto()).orElse(null);
            if (actualPontoColeta != null) {
                return ResponseEntity.ok(pontoRepository.save(pontoColeta));
            }
            return ResponseEntity.notFound().build();
        }
    }

