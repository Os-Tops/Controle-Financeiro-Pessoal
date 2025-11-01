package com.projeto.domains;

import com.projeto.domains.enums.Status;
import com.projeto.infra.StatusConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="contaBancaria")
@SequenceGenerator(
        name = "seq_contaBancaria", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_contaBancaria", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class ContaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contaBancaria")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=100)
    private String instituicao;

    @NotBlank
    @Column(nullable = false)
    private int agencia;

    @NotBlank
    @Column(nullable = false)
    private int numero;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal saldoInicial;

    @NotBlank
    @Column(nullable=false, length=100)
    private String apelido;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataSaldoInicial;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public ContaBancaria() {
    }

    public ContaBancaria(Long id, String instituicao, int agencia, int numero,
                         BigDecimal saldoInicial, String apelido, LocalDate dataSaldoInicial,
                         Status status, Usuario usuario) {
        this.id = id;
        this.instituicao = instituicao;
        this.agencia = agencia;
        this.numero = numero;
        this.saldoInicial = saldoInicial;
        this.apelido = apelido;
        this.dataSaldoInicial = dataSaldoInicial;
        this.status = status;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public LocalDate getDataSaldoInicial() {
        return dataSaldoInicial;
    }

    public void setDataSaldoInicial(LocalDate dataSaldoInicial) {
        this.dataSaldoInicial = dataSaldoInicial;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContaBancaria that = (ContaBancaria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
