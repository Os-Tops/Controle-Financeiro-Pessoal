package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FaturaCartaoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = CartaoCreditoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = CartaoCreditoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Competência é obrigatório")
    @JsonFormat(pattern = "dd")
    @Column(nullable = false)
    private LocalDate competencia = LocalDate.now();

    @NotBlank(message = "Data do Fechamento da Fatura é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataFechamentoFatura = LocalDate.now();

    @NotBlank(message = "Data do Vencimento da Fatura é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataVencimentoFatura = LocalDate.now();

    @Digits(integer = 12, fraction = 3, message = "Valor total deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Valor total não pode ser negativo")
    private BigDecimal valorTotal;

    @Min(value = 0, message = "Status da Fatura inválido: use 0 (ABERTA), 1 (FECHADA) ou 2 (PAGA)")
    @Max(value = 2, message = "Status da Fatura inválido: use 0 (ABERTA), 1 (FECHADA) ou 2 (PAGA)")
    private int statusFatura;

    @NotNull(message = "Cartão de Crédito é obrigatório")
    private Integer cartaoCreditoId;

    public FaturaCartaoDTO() {
    }

    public FaturaCartaoDTO(Long id, LocalDate competencia, LocalDate dataFechamentoFatura,
                           LocalDate dataVencimentoFatura,BigDecimal valorTotal, int statusFatura,
                           Integer cartaoCreditoId) {
        this.id = id;
        this.competencia = competencia;
        this.dataFechamentoFatura = dataFechamentoFatura;
        this.dataVencimentoFatura = dataVencimentoFatura;
        this.valorTotal = valorTotal;
        this.statusFatura = statusFatura;
        this.cartaoCreditoId = cartaoCreditoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCompetencia() {
        return competencia;
    }

    public void setCompetencia(LocalDate competencia) {
        this.competencia = competencia;
    }

    public LocalDate getDataFechamentoFatura() {
        return dataFechamentoFatura;
    }

    public void setDataFechamentoFatura(LocalDate dataFechamentoFatura) {
        this.dataFechamentoFatura = dataFechamentoFatura;
    }

    public LocalDate getDataVencimentoFatura() {
        return dataVencimentoFatura;
    }

    public void setDataVencimentoFatura(LocalDate dataVencimentoFatura) {
        this.dataVencimentoFatura = dataVencimentoFatura;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getStatusFatura() {
        return statusFatura;
    }

    public void setStatusFatura(int statusFatura) {
        this.statusFatura = statusFatura;
    }

    public Integer getCartaoCreditoId() {
        return cartaoCreditoId;
    }

    public void setCartaoCreditoId(Integer cartaoCreditoId) {
        this.cartaoCreditoId = cartaoCreditoId;
    }
}
