package com.projeto.domains;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Entidade")
@SequenceGenerator(
        name = "seq_Entidade",
        sequenceName = "seq_Entidade",
        allocationSize = 1
)

public class Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Entidade")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=120)
    private String nome;

    @NotBlank
    @Column(nullable=false, length=120)
    private String documento;

    @NotNull
    @Column(nullable=false, length=120)
    private Long idUsuario;

    public Entidade() {
    }

    public Entidade(Long id, String nome, String documento, Long idUsuario) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.idUsuario = idUsuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getdocumento() {
        return documento;
    }

    public void setdocumento(String documento) {
        this.documento = documento;
    }

    public Long getidUsuario() {
        return idUsuario;
    }

    public void setidUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entidade Entidade = (Entidade) o;
        return Objects.equals(id, Entidade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
