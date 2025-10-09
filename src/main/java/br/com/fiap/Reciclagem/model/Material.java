package br.com.fiap.Reciclagem.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tbl_material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MATERIAL")
    @SequenceGenerator(name = "SEQ_MATERIAL", sequenceName = "SEQ_MATERIAL", allocationSize = 1)
    @Column(name = "id_material")

    private Long idMaterial;

    private String nomeMaterial;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(idMaterial, material.idMaterial) && Objects.equals(nomeMaterial, material.nomeMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMaterial, nomeMaterial);
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNomeMaterial() {
        return nomeMaterial;
    }

    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }
}
