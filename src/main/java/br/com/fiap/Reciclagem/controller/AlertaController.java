package br.com.fiap.Reciclagem.controller;

import br.com.fiap.Reciclagem.model.Alerta;
import br.com.fiap.Reciclagem.model.Material;
import br.com.fiap.Reciclagem.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/alerta")
public class AlertaController {

    @Autowired
    private AlertaRepository alertaRepository;

    @GetMapping
    public List<Alerta> getAllAlerts() {
        return alertaRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<Alerta> save(@RequestBody Alerta alerta) {
        Alerta newAlerta = alertaRepository.save(alerta);
        return ResponseEntity.created(null).body(newAlerta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> getById(@PathVariable Long id) {
        Optional<Alerta> alerta = alertaRepository.findById(id);
        if (alerta.isPresent()) {
            return ResponseEntity.ok(alerta.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Alerta> deleteById(@PathVariable Long id) {
        Alerta alerta = alertaRepository.findById(id).orElse(null);
        if (alerta != null) {
            alertaRepository.delete(alerta);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Alerta> update(@RequestBody Alerta alerta) {
        Alerta actualAlerta = alertaRepository.findById(alerta.getIdAlerta()).orElse(null);
        if (actualAlerta != null) {
            return ResponseEntity.ok(alertaRepository.save(alerta));
        }
        return ResponseEntity.notFound().build();
    }
}
