package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class    CentroCustoDTO {

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

    @NotBlank(message = "usuario é obrigatório")
    @Size(max = 120, message = "usuario deve ter no máximo 120 caracteres")
    private String usuario;

    @NotBlank(message = "codigo é obrigatório")
    @Size(max = 120, message = "codigo deve ter no máximo 120 caracteres")
    private String codigo;

    @NotBlank(message = "status é obrigatório")
    @Size(max = 120, message = "status deve ter no máximo 120 caracteres")
    private String status;

    public CentroCustoDTO() {
    }

    public CentroCustoDTO(Long id, String nome, String usuario, String codigo, String status) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.codigo = codigo;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "CentroCustoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", usuario='" + usuario + '\'' +
                ", codigo='" + codigo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}