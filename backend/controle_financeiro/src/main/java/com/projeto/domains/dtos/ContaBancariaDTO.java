package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaBancariaDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = CartaoCreditoDTO.Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = CartaoCreditoDTO.Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "Instituição é obrigatória")
    @Size(max = 150, message = "Instituição deve ter no máximo 150 caracteres")
    private String instituicao;

    @NotNull(message = "Agência é obrigatório")
    private Integer agencia;

    @NotNull(message = "Número é obrigatório")
    private Integer numero;

    @NotBlank(message = "Apelido é obrigatória")
    @Size(max = 150, message = "Apelido deve ter no máximo 150 caracteres")
    private String apelido;

    @Digits(integer = 12, fraction = 3, message = "Saldo inicial deve ter no máximo 12 inteiros e 3 decimais")
    @PositiveOrZero(message = "Saldo inicial não pode ser negativo")
    private BigDecimal saldoInicial;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataSaldoInicial = LocalDate.now();

    @Min(value = 0, message = "Status do Cartão inválido: use 0 (ATIVA) ou 1 (INATIVA)")
    @Max(value = 1, message = "Status do Cartão inválido: use 0 (ATIVA) ou 1 (INATIVA)")
    private int status;

    @NotNull(message = "Usuario é obrigatório")
    private Integer usuarioId;

    public ContaBancariaDTO() {
    }

    public ContaBancariaDTO(Long id, String instituicao, Integer agencia,
                            Integer numero, String apelido, BigDecimal saldoInicial,
                            LocalDate dataSaldoInicial, int status, Integer usuarioId) {
        this.id = id;
        this.instituicao = instituicao;
        this.agencia = agencia;
        this.numero = numero;
        this.apelido = apelido;
        this.saldoInicial = saldoInicial;
        this.dataSaldoInicial = dataSaldoInicial;
        this.status = status;
        this.usuarioId = usuarioId;
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

    public Integer getAgencia() {
        return agencia;
    }

    public void setAgencia(Integer agencia) {
        this.agencia = agencia;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public LocalDate getDataSaldoInicial() {
        return dataSaldoInicial;
    }

    public void setDataSaldoInicial(LocalDate dataSaldoInicial) {
        this.dataSaldoInicial = dataSaldoInicial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
