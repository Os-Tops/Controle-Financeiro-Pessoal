package com.projeto.domains;

import com.projeto.domains.enums.StatusFatura;
import com.projeto.infra.StatusFaturaConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="faturaCartao")
@SequenceGenerator(
        name = "seq_faturaCartao", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_faturaCartao", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class FaturaCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_faturaCartao")
    private Long id;

    @JsonFormat(pattern = "MM/yyyy")
    @Column(nullable = false)
    private LocalDate competencia;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataFechamento;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataVencimento;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valorTotal;

    @Convert(converter = StatusFaturaConverter.class)
    @Column(name = "status", nullable = false)
    private StatusFatura statusFatura;

    @NotBlank
    @Column(nullable=false, length=100)
    private String instituicao;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCartao", nullable = false)
    private CartaoCredito cartaoCredito;

    public FaturaCartao() {
    }

    public FaturaCartao(Long id, LocalDate competencia, LocalDate dataFechamento, LocalDate dataVencimento, BigDecimal valorTotal, StatusFatura statusFatura, CartaoCredito cartaoCredito) {
        this.id = id;
        this.competencia = competencia;
        this.dataFechamento = dataFechamento;
        this.dataVencimento = dataVencimento;
        this.valorTotal = valorTotal;
        this.statusFatura = statusFatura;
        this.cartaoCredito = cartaoCredito;
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusFatura getStatusFatura() {
        return statusFatura;
    }

    public void setStatusFatura(StatusFatura statusFatura) {
        this.statusFatura = statusFatura;
    }

    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CartaoCredito cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FaturaCartao that = (FaturaCartao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
