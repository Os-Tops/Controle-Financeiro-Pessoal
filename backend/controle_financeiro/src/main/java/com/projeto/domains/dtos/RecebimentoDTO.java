package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class RecebimentoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Data do recebimento é obrigatório")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataRecebimento = LocalDate.now();

    @Digits(integer = 12, fraction = 3, message = "Valor recebido deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Valor recebido não pode ser negativo")
    private BigDecimal valorRecebido;

    @NotBlank(message = "Observação é obrigatório")
    @Size(max = 150, message = "Observação deve ter no máximo 150 caracteres")
    private String observacao;

    @NotNull(message = "Conta bancária é obrigatória")
    private Integer contaBancariaId;

    @NotNull(message = "Lançamento é obrigatório")
    private Integer lancamentoId;

    public RecebimentoDTO() {
    }

    public RecebimentoDTO(Long id, LocalDate dataRecebimento, BigDecimal valorRecebido,
                          String observacao, Integer contaBancariaId, Integer lancamentoId) {
        this.id = id;
        this.dataRecebimento = dataRecebimento;
        this.valorRecebido = valorRecebido;
        this.observacao = observacao;
        this.contaBancariaId = contaBancariaId;
        this.lancamentoId = lancamentoId;
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

    public Integer getContaBancariaId() {
        return contaBancariaId;
    }

    public void setContaBancariaId(Integer contaBancariaId) {
        this.contaBancariaId = contaBancariaId;
    }

    public Integer getLancamentoId() {
        return lancamentoId;
    }

    public void setLancamentoId(Integer lancamentoId) {
        this.lancamentoId = lancamentoId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecebimentoDTO that = (RecebimentoDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
