package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.domains.enums.StatusFatura;
import com.projeto.infra.StatusFaturaConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class    FaturaCartaoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotNull(message = "valorTotal é obrigatório")
    @Digits(integer = 10, fraction = 2, message = "valorTotal deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal valorTotal;

    @NotNull(message = "cartaoCredito é obrigatório")
    @Digits(integer = 10, fraction = 2, message = "cartaoCredito deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal cartaoCredito;

    @Convert(converter = StatusFaturaConverter.class)
    @Column(name = "status", nullable = false)
    private StatusFatura statusFatura;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate competencia = LocalDate.now();

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataFechamento = LocalDate.now();

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataVencimento = LocalDate.now();

    public FaturaCartaoDTO() {
    }

    public FaturaCartaoDTO(Long id,
                           BigDecimal valorTotal,
                           BigDecimal cartaoCredito,
                           StatusFatura statusFatura,
                           LocalDate competencia,
                           LocalDate dataFechamento,
                           LocalDate dataVencimento) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.cartaoCredito = cartaoCredito;
        this.statusFatura = statusFatura;
        this.competencia = competencia;
        this.dataFechamento = dataFechamento;
        this.dataVencimento = dataVencimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(BigDecimal cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public StatusFatura getStatusFatura() {
        return statusFatura;
    }

    public void setStatusFatura(StatusFatura statusFatura) {
        this.statusFatura = statusFatura;
    }

    public LocalDate getCompetencia() {
        return competencia;
    }

    public void setCompetencia(LocalDate competencia) {
        this.competencia = competencia;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Override
    public String toString() {
        return "FaturaCartaoDTO{" +
                "id=" + id +
                ", valorTotal=" + valorTotal +
                ", cartaoCredito=" + cartaoCredito +
                ", statusFatura=" + statusFatura +
                ", competencia=" + competencia +
                ", dataFechamento=" + dataFechamento +
                ", dataVencimento=" + dataVencimento +
                '}';
    }
}