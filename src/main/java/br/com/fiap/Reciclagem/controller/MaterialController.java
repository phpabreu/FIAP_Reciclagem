package br.com.fiap.Reciclagem.controller;
import br.com.fiap.Reciclagem.model.Material;
import br.com.fiap.Reciclagem.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<Material> save(@RequestBody Material material) {
        Material newMaterial = materialRepository.save(material);
        return ResponseEntity.created(null).body(newMaterial);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        Optional<Material> material = materialRepository.findById(id);
        if (material.isPresent()) {
            return ResponseEntity.ok(material.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Material> deleteById(@PathVariable Long id) {
        Material material = materialRepository.findById(id).orElse(null);
        if (material != null) {
            materialRepository.delete(material);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Material> update(@RequestBody Material material) {
        Material actualMaterial = materialRepository.findById(material.getIdMaterial()).orElse(null);
        if (actualMaterial != null) {
            return ResponseEntity.ok(materialRepository.save(material));
        }
        return ResponseEntity.notFound().build();
    }
}




