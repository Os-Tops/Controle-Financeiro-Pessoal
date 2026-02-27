package com.projeto.domains.dtos;

import jakarta.validation.constraints.*;

public class EntidadeDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = EntidadeDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = EntidadeDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome da entidade é obrigatória")
    @Size(max = 150, message = "Nome da entidade deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "Documento é obrigatório")
    @Size(max = 150, message = "Documento deve ter no máximo 50 caracteres")
    private String documento;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    public EntidadeDTO() {
    }

    public EntidadeDTO(Long id, String nome, String documento, Integer usuarioId) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.usuarioId = usuarioId;
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
