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
@Table(name="recebimento")
@SequenceGenerator(
        name = "seq_recebimento", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_recebimento", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class Recebimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recebimento")
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataRecebimento;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valorRecebido;

    @NotBlank
    @Column(nullable=false, length=100)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idConta", nullable = false)
    private ContaBancaria contaBancaria;

    public Recebimento() {
    }

    public Recebimento(Long id, LocalDate dataRecebimento, BigDecimal valorRecebido,
                       String observacao, ContaBancaria contaBancaria) {
        this.id = id;
        this.dataRecebimento = dataRecebimento;
        this.valorRecebido = valorRecebido;
        this.observacao = observacao;
        this.contaBancaria = contaBancaria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
        Recebimento that = (Recebimento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
