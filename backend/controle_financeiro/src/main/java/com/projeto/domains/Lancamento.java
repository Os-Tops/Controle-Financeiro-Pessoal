package com.projeto.domains;

import com.projeto.domains.enums.MeioPagamento;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import com.projeto.infra.MeioPagamentoConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.projeto.infra.StatusLancamentoConverter;
import com.projeto.infra.TipoLancamentoConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="lancamento")
@SequenceGenerator(
        name = "seq_lancamento", // mesmo nome usado no @GeneratedValue
        sequenceName = "seq_lancamento", // nome da sequência no banco
        allocationSize = 1 // incrementa de 1 em 1 (evita “saltos”)
)

public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lancamento")
    private Long id;

    @NotBlank
    @Column(nullable=false, length=100)
    private String descricao;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataCompetencia;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataVencimento;

    @NotNull
    @Digits(integer = 15, fraction = 3)
    @Column(precision = 18, scale = 3, nullable = false)
    private BigDecimal valorBaixado;

    @Convert(converter = StatusLancamentoConverter.class)
    @Column(name = "statusLancamento", nullable = false)
    private StatusLancamento statusLancamento;

    @Convert(converter = MeioPagamentoConverter.class)
    @Column(name = "meioPagamento", nullable = false)
    private MeioPagamento meioPagamento;

    @Convert(converter = TipoLancamentoConverter.class)
    @Column(name = "tipoLancamento", nullable = false)
    private TipoLancamento tipoLancamento;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContaBancaria", nullable = false)
    private ContaBancaria contaBancaria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCentroCusto", nullable = false)
    private CentroCusto centroCusto;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEntidade", nullable = false)
    private Entidade entidade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCartaoCredito", nullable = false)
    private CartaoCredito cartaoCredito;

    public Lancamento() {
    }

    public Lancamento(Long id, String descricao, BigDecimal valor, LocalDate dataCompetencia,
                      LocalDate dataVencimento, BigDecimal valorBaixado, StatusLancamento statusLancamento,
                      MeioPagamento meioPagamento, TipoLancamento tipoLancamento, Usuario usuario,
                      ContaBancaria contaBancaria, CentroCusto centroCusto, Entidade entidade, CartaoCredito cartaoCredito) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataCompetencia = dataCompetencia;
        this.dataVencimento = dataVencimento;
        this.valorBaixado = valorBaixado;
        this.statusLancamento = statusLancamento;
        this.meioPagamento = meioPagamento;
        this.tipoLancamento = tipoLancamento;
        this.usuario = usuario;
        this.contaBancaria = contaBancaria;
        this.centroCusto = centroCusto;
        this.entidade = entidade;
        this.cartaoCredito = cartaoCredito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataCompetencia() {
        return dataCompetencia;
    }

    public void setDataCompetencia(LocalDate dataCompetencia) {
        this.dataCompetencia = dataCompetencia;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValorBaixado() {
        return valorBaixado;
    }

    public void setValorBaixado(BigDecimal valorBaixado) {
        this.valorBaixado = valorBaixado;
    }

    public StatusLancamento getStatusLancamento() {
        return statusLancamento;
    }

    public void setStatusLancamento(StatusLancamento statusLancamento) {
        this.statusLancamento = statusLancamento;
    }

    public MeioPagamento getMeioPagamento() {
        return meioPagamento;
    }

    public void setMeioPagamento(MeioPagamento meioPagamento) {
        this.meioPagamento = meioPagamento;
    }

    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
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
        Lancamento that = (Lancamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
