package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CartaoCreditoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Bandeira é obrigatória")
    @Size(max = 150, message = "Bandeira deve ter no máximo 150 caracteres")
    private String bandeira;

    @NotBlank(message = "Emissor é obrigatório")
    @Size(max = 150, message = "Emissor deve ter no máximo 50 caracteres")
    private String emissor;

    @NotBlank(message = "Apelido é obrigatório")
    @Size(max = 150, message = "Apelido deve ter no máximo 50 caracteres")
    private String apelido;

    @NotNull(message = "Dia do Fechamento da Fatura é obrigatório")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechamentoFaturaDia = LocalDate.now();

    @NotNull(message = "Dia do Vencimento da Fatura é obrigatório")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate vencimentoFaturaDia = LocalDate.now();

    @Min(value = 0, message = "Status do Cartão inválido: use 0 (DESBLOQUEADO) ou 1 (BLOQUEADO)")
    @Max(value = 1, message = "Status do Cartão inválido: use 0 (DESBLOQUEADO) ou 1 (BLOQUEADO)")
    private Integer statusCartao;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    public CartaoCreditoDTO() {
    }

    public CartaoCreditoDTO(Long id,
                            String bandeira,
                            String emissor,
                            String apelido,
                            LocalDate fechamentoFaturaDia,
                            LocalDate vencimentoFaturaDia,
                            Integer statusCartao,
                            Integer usuarioId) {
        this.id = id;
        this.bandeira = bandeira;
        this.emissor = emissor;
        this.apelido = apelido;
        this.fechamentoFaturaDia = fechamentoFaturaDia;
        this.vencimentoFaturaDia = vencimentoFaturaDia;
        this.statusCartao = statusCartao;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public LocalDate getFechamentoFaturaDia() {
        return fechamentoFaturaDia;
    }

    public void setFechamentoFaturaDia(LocalDate fechamentoFaturaDia) {
        this.fechamentoFaturaDia = fechamentoFaturaDia;
    }

    public LocalDate getVencimentoFaturaDia() {
        return vencimentoFaturaDia;
    }

    public void setVencimentoFaturaDia(LocalDate vencimentoFaturaDia) {
        this.vencimentoFaturaDia = vencimentoFaturaDia;
    }

    public Integer getStatusCartao() {
        return statusCartao;
    }

    public void setStatusCartao(Integer statusCartao) {
        this.statusCartao = statusCartao;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
