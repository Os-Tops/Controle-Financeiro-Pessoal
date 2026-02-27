package com.projeto.domains.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class TelefoneUsuarioDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = TelefoneUsuarioDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = TelefoneUsuarioDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "DDD do telefone é obrigatório")
    @Size(max = 2, message = "DDD do telefone deve ter no máximo 2 caracteres")
    private String ddd;

    @NotBlank(message = "Número do telefone é obrigatório")
    @Size(max = 9, message = "Número deve ter no máximo 9 caracteres")
    private String numero;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    public TelefoneUsuarioDTO() {
    }

    public TelefoneUsuarioDTO(Long id, String ddd, String numero, Integer usuarioId) {
        this.id = id;
        this.ddd = ddd;
        this.numero = numero;
        this.usuarioId = usuarioId;
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
