package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class    EntidadeDTO {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    private String nome;

    @NotBlank(message = "documento é obrigatório")
    @Size(max = 120, message = "documento deve ter no máximo 120 caracteres")
    private String documento;

    @NotBlank(message = "usuario é obrigatório")
    @Size(max = 120, message = "usuario deve ter no máximo 120 caracteres")
    private String usuario;

    public EntidadeDTO() {
    }

    public EntidadeDTO(Long id, String usuario, String documento, String nome) {
        this.id = id;
        this.usuario = usuario;
        this.documento = documento;
        this.nome = nome;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "EntidadeDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento='" + documento + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}