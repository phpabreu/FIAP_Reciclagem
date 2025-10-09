package br.com.fiap.Reciclagem.controller;

import br.com.fiap.Reciclagem.model.Alerta;
import br.com.fiap.Reciclagem.model.PontoColeta;
import br.com.fiap.Reciclagem.model.Recipiente;
import br.com.fiap.Reciclagem.repository.AlertaRepository;
import br.com.fiap.Reciclagem.repository.PontoRepository;
import br.com.fiap.Reciclagem.repository.RecipienteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipiente")
public class RecipienteController {

    @Autowired
    private RecipienteRepository recipienteRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private PontoRepository pontoColetaRepository;



    @GetMapping
    public List<Recipiente> getRecipientes() {
        return recipienteRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<Recipiente> save(@RequestBody Recipiente recipiente) {
        if (isCapacidadeInvalida(recipiente)) {
            return ResponseEntity.badRequest().body(null);
        }
        Recipiente newRecipiente = recipienteRepository.save(recipiente);
        verificarVolumeEEmitirAlerta(newRecipiente);
        return ResponseEntity.created(null).body(newRecipiente);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recipiente> getById(@PathVariable Long id) {
        Optional<Recipiente> recipiente = recipienteRepository.findById(id);
        if (recipiente.isPresent()) {
            return ResponseEntity.ok(recipiente.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Recipiente> deleteById(@PathVariable Long id) {
        Recipiente recipiente = recipienteRepository.findById(id).orElse(null);
        if (recipiente != null) {
            recipienteRepository.delete(recipiente);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipiente> update(@PathVariable Long id, @RequestBody Recipiente recipiente) {
        if (!recipienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        recipiente.setIdRecipiente(id);

        if (isCapacidadeInvalida(recipiente)) {
            return ResponseEntity.badRequest().body(null);
        }

        Recipiente updated = recipienteRepository.save(recipiente);

        verificarVolumeEEmitirAlerta(updated);

        return ResponseEntity.ok(updated);
    }




    private void verificarVolumeEEmitirAlerta(Recipiente recipiente) {
        double porcentagem = (recipiente.getVolumeAtual() / recipiente.getCapacidadeMax()) * 100;


        if (porcentagem > 70) {
            Long idPonto = recipiente.getIdPonto().getIdPonto();

            Optional<PontoColeta> optionalPonto = pontoColetaRepository.findById(idPonto);


            if (optionalPonto.isPresent()) {
                PontoColeta pontoColeta = optionalPonto.get();

                Alerta alerta = new Alerta();
                alerta.setIdPonto(recipiente.getIdPonto());
                alerta.setMensagem("Recipiente " + recipiente.getIdRecipiente() +
                        " localizado no endereço: " + pontoColeta.getEndereco() + ", " +
                        pontoColeta.getBairro() + ", " + pontoColeta.getCidade() + ", " +
                        pontoColeta.getEstado() + " atingiu " + Math.round(porcentagem) + "% da capacidade.");
                alerta.setUltimaAtualizacao(LocalDate.now());

                alertaRepository.save(alerta);
            } else {
                System.out.println("Ponto de coleta não encontrado para o ID: " + recipiente.getIdPonto());
            }
        }
    }

    private boolean isCapacidadeInvalida(Recipiente recipiente) {
        return recipiente.getCapacidadeMax() == null || recipiente.getCapacidadeMax() <= 0 || recipiente.getCapacidadeMax() > 100;
    }


}
