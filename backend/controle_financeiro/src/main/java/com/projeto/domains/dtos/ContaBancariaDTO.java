package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class    ContaBancariaDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "instituicao é obrigatório")
    @Size(max = 120, message = "instituicao deve ter no máximo 120 caracteres")
    private String instituicao;

    @NotBlank(message = "agencia é obrigatório")
    @Size(max = 120, message = "agencia deve ter no máximo 120 caracteres")
    private int agencia;

    @NotBlank(message = "agencia é obrigatório")
    @Size(max = 120, message = "agencia deve ter no máximo 120 caracteres")
    private int numero;

    @NotNull(message = "saldoInicial é obrigatório")
    @Digits(integer = 10, fraction = 2, message = "saldoInicial deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal saldoInicial;

    @NotBlank(message = "agencia é obrigatório")
    @Size(max = 120, message = "agencia deve ter no máximo 120 caracteres")
    private String apelido;

    @NotBlank(message = "agencia é obrigatório")
    @Size(max = 120, message = "agencia deve ter no máximo 120 caracteres")
    private String usuario;

    @NotBlank(message = "status é obrigatório")
    @Size(max = 120, message = "status deve ter no máximo 120 caracteres")
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataSaldoInicial = LocalDate.now();

    public ContaBancariaDTO() {
    }

    public ContaBancariaDTO(Long id,
                            String instituicao,
                            int agencia,
                            int numero,
                            BigDecimal saldoInicial,
                            String apelido,
                            String usuario,
                            String status,
                            LocalDate dataSaldoInicial) {
        this.id = id;
        this.instituicao = instituicao;
        this.agencia = agencia;
        this.numero = numero;
        this.saldoInicial = saldoInicial;
        this.apelido = apelido;
        this.usuario = usuario;
        this.status = status;
        this.dataSaldoInicial = dataSaldoInicial;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataSaldoInicial() {
        return dataSaldoInicial;
    }

    public void setDataSaldoInicial(LocalDate dataSaldoInicial) {
        this.dataSaldoInicial = dataSaldoInicial;
    }

    @Override
    public String toString() {
        return "ContaBancariaDTO{" +
                "id=" + id +
                ", instituicao='" + instituicao + '\'' +
                ", agencia=" + agencia +
                ", numero=" + numero +
                ", saldoInicial=" + saldoInicial +
                ", apelido='" + apelido + '\'' +
                ", usuario='" + usuario + '\'' +
                ", status='" + status + '\'' +
                ", dataSaldoInicial=" + dataSaldoInicial +
                '}';
    }
}
