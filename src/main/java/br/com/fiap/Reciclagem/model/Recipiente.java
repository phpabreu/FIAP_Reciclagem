package br.com.fiap.Reciclagem.model;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "tbl_recipiente")

public class Recipiente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPIENTE_SEQ")
    @SequenceGenerator(name = "RECIPIENTE_SEQ", sequenceName = "RECIPIENTE_SEQ", allocationSize = 1)
    @Column(name = "id_recipiente")
    private Long idRecipiente;


    @ManyToOne
    @JoinColumn(name = "id_ponto", nullable = false)
    private PontoColeta pontoColeta;


    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private Material idMaterial;

    private Double capacidadeMax;

    private Double volumeAtual;

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;


    public Long getIdRecipiente() {
        return idRecipiente;
    }

    public void setIdRecipiente(Long idRecipiente) {
        this.idRecipiente = idRecipiente;
    }

    public PontoColeta getIdPonto() {
        return pontoColeta;
    }

    public void setIdPonto(PontoColeta idPonto) {
        this.pontoColeta = idPonto;
    }


    public Material getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Material idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Double getCapacidadeMax() {
        return capacidadeMax;
    }

    public void setCapacidadeMax(Double capacidadeMax) {
        this.capacidadeMax = capacidadeMax;
    }

    public Double getVolumeAtual() {
        return volumeAtual;
    }

    public void setVolumeAtual(Double volumeAtual) {
        this.volumeAtual = volumeAtual;
    }

    public LocalDate getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDate ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipiente that = (Recipiente) o;
        return Objects.equals(idRecipiente, that.idRecipiente) && Objects.equals(pontoColeta, that.pontoColeta) && Objects.equals(idMaterial, that.idMaterial) && Objects.equals(capacidadeMax, that.capacidadeMax) && Objects.equals(volumeAtual, that.volumeAtual) && Objects.equals(ultimaAtualizacao, that.ultimaAtualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRecipiente, pontoColeta, idMaterial, capacidadeMax, volumeAtual, ultimaAtualizacao);
    }


}