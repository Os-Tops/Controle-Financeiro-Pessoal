package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


public class LancamentoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    private String descricao;

    @Digits(integer = 12, fraction = 3, message = "Valor total deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Valor total não pode ser negativo")
    private BigDecimal valor;

    @NotBlank(message = "Data da competência é obrigatória")
    @JsonFormat(pattern = "dd")
    @Column(nullable = false)
    private LocalDate dataCompetencia = LocalDate.now();

    @NotBlank(message = "Data de Vencimento é obrigatória")
    @JsonFormat(pattern = "dd")
    @Column(nullable = false)
    private LocalDate dataVencimento = LocalDate.now();

    @Digits(integer = 12, fraction = 3, message = "Valor baixado deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Valor baixado não pode ser negativo")
    private BigDecimal valorBaixado;

    @Min(value = 0, message = "Status do Lançamento inválido: use 0 (PENDENTE), 1 (BAIXADO), 2 (PARCIAL) ou 3 (CANCELADO)")
    @Max(value = 1, message = "Status do Lançamento inválido: use 0 (PENDENTE), 1 (BAIXADO), 2 (PARCIAL) ou 3 (CANCELADO)")
    @Max(value = 2, message = "Status do Lançamento inválido: use 0 (PENDENTE), 1 (BAIXADO), 2 (PARCIAL) ou 3 (CANCELADO)")
    @Max(value = 3, message = "Status do Lançamento inválido: use 0 (PENDENTE), 1 (BAIXADO), 2 (PARCIAL) ou 3 (CANCELADO)")
    private int StatusLancamento;

    @Min(value = 0, message = "Meio de pagamento inválido: use 0 (CONTA), 1 (CARTAO), 2 (DINHEIRO) ou 3 (PIX)")
    @Max(value = 1, message = "Meio de pagamento inválido: use 0 (CONTA), 1 (CARTAO), 2 (DINHEIRO) ou 3 (PIX)")
    @Max(value = 2, message = "Meio de pagamento inválido: use 0 (CONTA), 1 (CARTAO), 2 (DINHEIRO) ou 3 (PIX)")
    @Max(value = 3, message = "Meio de pagamento inválido: use 0 (CONTA), 1 (CARTAO), 2 (DINHEIRO) ou 3 (PIX)")
    private int MeioPagamento;

    @Min(value = 0, message = "Tipo de lançamento inválido: use 0 (PAGAR), 1 (RECEBER)")
    @Max(value = 1, message = "Tipo de lançamento inválido: use 0 (PAGAR), 1 (RECEBER)")
    private int TipoLancamento;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    @NotNull(message = "Conta bancária é obrigatória")
    private Integer contaBancariaId;

    @NotNull(message = "Centro de Custo é obrigatório")
    private Integer centroCustoId;

    @NotNull(message = "Entidade é obrigatória")
    private Integer entidadeId;

    @NotNull(message = "Cartão de crédito é obrigatório")
    private Integer cartaoCreditoId;

    public LancamentoDTO() {
    }

    public LancamentoDTO(Long id, String descricao, BigDecimal valor,
                         LocalDate dataCompetencia, LocalDate dataVencimento, BigDecimal valorBaixado, int statusLancamento, int meioPagamento, int tipoLancamento, Integer usuarioId, Integer contaBancariaId, Integer centroCustoId, Integer entidadeId, Integer cartaoCreditoId) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataCompetencia = dataCompetencia;
        this.dataVencimento = dataVencimento;
        this.valorBaixado = valorBaixado;
        StatusLancamento = statusLancamento;
        MeioPagamento = meioPagamento;
        TipoLancamento = tipoLancamento;
        this.usuarioId = usuarioId;
        this.contaBancariaId = contaBancariaId;
        this.centroCustoId = centroCustoId;
        this.entidadeId = entidadeId;
        this.cartaoCreditoId = cartaoCreditoId;
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

    public int getStatusLancamento() {
        return StatusLancamento;
    }

    public void setStatusLancamento(int statusLancamento) {
        StatusLancamento = statusLancamento;
    }

    public int getMeioPagamento() {
        return MeioPagamento;
    }

    public void setMeioPagamento(int meioPagamento) {
        MeioPagamento = meioPagamento;
    }

    public int getTipoLancamento() {
        return TipoLancamento;
    }

    public void setTipoLancamento(int tipoLancamento) {
        TipoLancamento = tipoLancamento;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getContaBancariaId() {
        return contaBancariaId;
    }

    public void setContaBancariaId(Integer contaBancariaId) {
        this.contaBancariaId = contaBancariaId;
    }

    public Integer getCentroCustoId() {
        return centroCustoId;
    }

    public void setCentroCustoId(Integer centroCustoId) {
        this.centroCustoId = centroCustoId;
    }

    public Integer getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(Integer entidadeId) {
        this.entidadeId = entidadeId;
    }

    public Integer getCartaoCreditoId() {
        return cartaoCreditoId;
    }

    public void setCartaoCreditoId(Integer cartaoCreditoId) {
        this.cartaoCreditoId = cartaoCreditoId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LancamentoDTO that = (LancamentoDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
