package com.projeto.domains;

import com.projeto.domains.enums.TipoTransacao;
import com.projeto.infra.TipoTransacaoConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="movimentoConta")
@SequenceGenerator(
        name = "seq_movimentoConta", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_movimentoConta", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class MovimentoConta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movimentoConta")
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataMovimento;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    @NotBlank
    @Column(nullable=false, length=100)
    private String historico;

    @Convert(converter = TipoTransacaoConverter.class)
    @Column(name = "status", nullable = false)
    private TipoTransacao tipoTransacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idConta", nullable = false)
    private ContaBancaria contaBancaria;

    public MovimentoConta() {
    }

    public MovimentoConta(Long id, LocalDate dataMovimento,
                          BigDecimal valor, String historico,
                          TipoTransacao tipoTransacao, ContaBancaria contaBancaria) {
        this.id = id;
        this.dataMovimento = dataMovimento;
        this.valor = valor;
        this.historico = historico;
        this.tipoTransacao = tipoTransacao;
        this.contaBancaria = contaBancaria;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDate dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoConta that = (MovimentoConta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
