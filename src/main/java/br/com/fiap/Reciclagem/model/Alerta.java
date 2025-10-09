package br.com.fiap.Reciclagem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tbl_alerta")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALERTA")
    @SequenceGenerator(name = "SEQ_ALERTA", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "id_alerta")

    private Long idAlerta ;

    @ManyToOne
    @JoinColumn(name = "id_ponto", nullable = false)
    private PontoColeta idPonto;

    private String mensagem;

    @Column(name = "ultima_atualizacao")
    private LocalDate ultimaAtualizacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return Objects.equals(idAlerta, alerta.idAlerta) && Objects.equals(idPonto, alerta.idPonto) && Objects.equals(mensagem, alerta.mensagem) && Objects.equals(ultimaAtualizacao, alerta.ultimaAtualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlerta, idPonto, mensagem, ultimaAtualizacao);
    }

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    public PontoColeta getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(PontoColeta idPonto) {
        this.idPonto = idPonto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDate ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
