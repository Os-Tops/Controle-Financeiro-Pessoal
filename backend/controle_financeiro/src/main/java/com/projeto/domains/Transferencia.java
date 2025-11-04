package com.projeto.domains;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Transferencia")
@SequenceGenerator(
        name = "seq_transferencia", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_transferencia", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transferencia")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContaBancaria", nullable = false)
    private ContaBancaria contaOrigem ;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContaBancaria", nullable = false)
    private ContaBancaria contaDestino ;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate data;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    @NotBlank
    @Column(nullable=false, length=120)
    private String observacao;

    public Transferencia() {
    }

    public Transferencia(Long id, ContaBancaria contaOrigem, ContaBancaria contaDestino, LocalDate data, BigDecimal valor, String observacao) {
        this.id = id;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaBancaria getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(ContaBancaria contaDestino) {
        this.contaDestino = contaDestino;
    }

    public ContaBancaria getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(ContaBancaria contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transferencia that = (Transferencia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
