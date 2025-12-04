package com.projeto.domains.dtos;

import jakarta.validation.constraints.*;

public class CentroCustoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = CentroCustoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = CentroCustoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Nome da entidade é obrigatória")
    @Size(max = 150, message = "Nome da entidade deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Código é obrigatório")
    private Integer codigo;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    @Min(value = 0, message = "Status inválido: use 0 (ATIVO) ou 1 (INATIVO)")
    @Max(value = 1, message = "Status inválido: use 0 (ATIVO) ou 1 (INATIVO)")
    private Integer status;

    public CentroCustoDTO() {}

    public CentroCustoDTO(Long id, String nome, Integer codigo, Integer usuarioId, Integer status) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.usuarioId = usuarioId;
        this.status = status;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public Integer getCodigo() { return codigo; }

    public void setCodigo(Integer codigo) { this.codigo = codigo; }

    public Integer getUsuarioId() { return usuarioId; }

    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}
