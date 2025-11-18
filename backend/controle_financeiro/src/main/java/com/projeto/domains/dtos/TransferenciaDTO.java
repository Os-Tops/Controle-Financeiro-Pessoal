package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class TransferenciaDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotNull(message = "Conta de origem é obrigatória")
    private Long contaOrigemId;

    @NotNull(message = "Conta de destino é obrigatória")
    private Long contaDestinoId;

    @NotBlank(message = "Data é obrigatório")
    @JsonFormat(pattern = "dd")
    @Column(nullable = false)
    private LocalDate data = LocalDate.now();

    @Digits(integer = 12, fraction = 3, message = "Valor total deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Valor total não pode ser negativo")
    private BigDecimal valor;

    @NotBlank(message = "Observação é obrigatória")
    @Size(max = 150, message = "Observação deve ter no máximo 150 caracteres")
    private String observacao;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Long id, Long contaOrigemId,
                            Long contaDestinoId, LocalDate data,
                            BigDecimal valor, String observacao) {
        this.id = id;
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
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

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(Long contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
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
        TransferenciaDTO that = (TransferenciaDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
