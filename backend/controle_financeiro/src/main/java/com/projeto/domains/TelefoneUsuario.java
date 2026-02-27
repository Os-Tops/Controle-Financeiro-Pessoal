package com.projeto.domains;

import java.time.LocalDate;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="telefoneUsuario")
@SequenceGenerator(
        name = "seq_telefoneUsuario", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_telefoneUsuario", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class TelefoneUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_telefoneUsuario")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=2)
    private String ddd;

    @NotBlank
    @Column(nullable=false, length=9)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public TelefoneUsuario() {
    }

    public TelefoneUsuario(Long id, String ddd, String numero, Usuario usuario) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TelefoneUsuario telefoneUsuario = (TelefoneUsuario) o;
        return Objects.equals(id, telefoneUsuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
