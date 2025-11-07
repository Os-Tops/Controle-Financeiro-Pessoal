package com.projeto.domains.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.domains.Usuario;
import com.projeto.domains.enums.StatusCartao;
import com.projeto.infra.StatusCartaoConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class   CartaoCreditoDTO {

    public interface Create {}
    public interface Update {}

    @Null(groups = Create.class, message = "Id deve ser omitido na criação")
    @NotNull(groups = Update.class, message = "Id é obrigatório na atualização")
    private Long id;

    @NotBlank(message = "bandeira é obrigatório")
    @Size(max = 120, message = "bandeira deve ter no máximo 120 caracteres")
    private String bandeira;

    @NotBlank(message = "emissor é obrigatório")
    @Size(max = 120, message = "emissor deve ter no máximo 120 caracteres")
    private String emissor;

    @NotBlank(message = "apelido é obrigatório")
    @Size(max = 120, message = "apelido deve ter no máximo 120 caracteres")
    private String apelido;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fechamentoFaturaDia = LocalDate.now();

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate vencimentoFaturaDia = LocalDate.now();

    @Convert(converter = StatusCartaoConverter.class)
    @Column(name = "status", nullable = false)
    private StatusCartao statusCartao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)

    private Usuario usuario;

    public CartaoCreditoDTO(Long id,
                            String bandeira,
                            String emissor,
                            String apelido,
                            LocalDate fechamentoFaturaDia,
                            StatusCartao statusCartao,
                            LocalDate vencimentoFaturaDia,
                            Usuario usuario) {
        this.id = id;
        this.bandeira = bandeira;
        this.emissor = emissor;
        this.apelido = apelido;
        this.fechamentoFaturaDia = fechamentoFaturaDia;
        this.statusCartao = statusCartao;
        this.vencimentoFaturaDia = vencimentoFaturaDia;
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
    public String toString() {
        return "CartaoCreditoDTO{" +
                "id=" + id +
                ", bandeira='" + bandeira + '\'' +
                ", emissor='" + emissor + '\'' +
                ", apelido='" + apelido + '\'' +
                ", fechamentoFaturaDia=" + fechamentoFaturaDia +
                ", vencimentoFaturaDia=" + vencimentoFaturaDia +
                ", statusCartao=" + statusCartao +
                ", usuario=" + usuario +
                '}';
    }
}