package com.projeto.domains;

import com.projeto.domains.enums.StatusCartao;
import com.projeto.infra.StatusCartaoConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="cartaoCredito")
@SequenceGenerator(
        name = "seq_cartaoCredito", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_cartaoCredito", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class CartaoCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cartaoCredito")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=100)
    private String bandeira;

    @NotBlank
    @Column(nullable=false, length=100)
    private String emissor;

    @NotBlank
    @Column(nullable=false, length=100)
    private String apelido;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fechamentoFaturaDia;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate vencimentoFaturaDia;


    @Convert(converter = StatusCartaoConverter.class)
    @Column(name = "status", nullable = false)
    private StatusCartao statusCartao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public CartaoCredito() {
    }

    public CartaoCredito(Long id, String bandeira, String emissor,
                         String apelido, LocalDate fechamentoFaturaDia,
                         LocalDate vencimentoFaturaDia,
                         StatusCartao statusCartao, Usuario usuario) {
        this.id = id;
        this.bandeira = bandeira;
        this.emissor = emissor;
        this.apelido = apelido;
        this.fechamentoFaturaDia = fechamentoFaturaDia;
        this.vencimentoFaturaDia = vencimentoFaturaDia;
        this.statusCartao = statusCartao;
        this.usuario = usuario;
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

    public StatusCartao getStatusCartao() {
        return statusCartao;
    }

    public void setStatusCartao(StatusCartao statusCartao) {
        this.statusCartao = statusCartao;
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
        CartaoCredito that = (CartaoCredito) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
