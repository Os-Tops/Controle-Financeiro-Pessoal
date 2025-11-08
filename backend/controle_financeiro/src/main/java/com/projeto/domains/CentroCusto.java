package com.projeto.domains;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.domains.enums.Status;
import com.projeto.infra.StatusConverter;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="CentroCusto")
@SequenceGenerator(
        name = "seq_CentroCusto",
        sequenceName = "seq_CentroCusto",
        allocationSize = 1
)

public class CentroCusto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_CentroCusto")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=120)
    private String nome;

    @NotNull
    @Column(nullable=false, length=120)
    private int codigo;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public CentroCusto() {
    }

    public CentroCusto(Long id, String nome, int codigo, Usuario usuario, Status status) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.usuario = usuario;
        this.status = status;
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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CentroCusto that = (CentroCusto) o;
        return codigo == that.codigo && Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && status == that.status && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}