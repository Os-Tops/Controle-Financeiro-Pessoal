package com.projeto.domains.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class CentroCustoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = CartaoCreditoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = CartaoCreditoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome da entidade é obrigatória")
    @Size(max = 150, message = "Nome da entidade deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Código é obrigatório")
    private Integer codigo;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    public CentroCustoDTO() {
    }

    public CentroCustoDTO(Long id, String nome, Integer codigo, Integer usuarioId) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
