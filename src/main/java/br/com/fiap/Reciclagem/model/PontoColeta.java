package br.com.fiap.Reciclagem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tbl_ponto_coleta")
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PONTO_COLETA")
    @SequenceGenerator(name = "SEQ_PONTO_COLETA", sequenceName = "SEQ_PONTO_COLETA", allocationSize = 1)
    @Column(name = "id_ponto")
    private Long idPonto;

    private String nome;

    private String endereco;

    private String bairro;

    private String cidade;

    private String estado;

    private String cep;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PontoColeta that = (PontoColeta) o;
        return Objects.equals(idPonto, that.idPonto) && Objects.equals(nome, that.nome) && Objects.equals(endereco, that.endereco) && Objects.equals(bairro, that.bairro) && Objects.equals(cidade, that.cidade) && Objects.equals(estado, that.estado) && Objects.equals(cep, that.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPonto, nome, endereco, bairro, cidade, estado, cep);
    }

    public Long getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


}
