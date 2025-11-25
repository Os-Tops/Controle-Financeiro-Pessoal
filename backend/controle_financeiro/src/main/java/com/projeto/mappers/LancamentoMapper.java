package com.projeto.mappers;

import com.projeto.domains.Lancamento;
import com.projeto.domains.Usuario;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.CentroCusto;
import com.projeto.domains.Entidade;
import com.projeto.domains.CartaoCredito;
import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.MeioPagamento;
import com.projeto.domains.enums.TipoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LancamentoMapper {

    private LancamentoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static LancamentoDTO toDto(Lancamento e) {
        if (e == null) return null;

        Long idDto = e.getId();

        Integer usuarioId = Math.toIntExact((e.getUsuario() == null) ? null : e.getUsuario().getId());
        Integer contaBancariaId = Math.toIntExact((e.getContaBancaria() == null) ? null : e.getContaBancaria().getId());
        Integer centroCustoId = Math.toIntExact((e.getCentroCusto() == null) ? null : e.getCentroCusto().getId());
        Integer entidadeId = Math.toIntExact((e.getEntidade() == null) ? null : e.getEntidade().getId());
        Integer cartaoCreditoId = Math.toIntExact((e.getCartaoCredito() == null) ? null : e.getCartaoCredito().getId());
        int statusLancamentoInt = (e.getStatusLancamento() == null) ? 0 : e.getStatusLancamento().getId();
        int meioPagamentoInt = (e.getMeioPagamento() == null) ? 0 : e.getMeioPagamento().getId();
        int tipoLancamentoInt = (e.getTipoLancamento() == null) ? 0 : e.getTipoLancamento().getId();

        return new LancamentoDTO(
                idDto,
                e.getDescricao(),
                e.getValor(),
                e.getDataCompetencia(),
                e.getDataVencimento(),
                e.getValorBaixado(),
                statusLancamentoInt,
                meioPagamentoInt,
                tipoLancamentoInt,
                usuarioId,
                contaBancariaId,
                centroCustoId,
                entidadeId,
                cartaoCreditoId
        );
    }
    public static List<LancamentoDTO> toDtoList(Collection<Lancamento> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(LancamentoMapper::toDto)
                .collect(Collectors.toList());
    }
    public static Page<LancamentoDTO> toDtoPage(Page<Lancamento> page) {
        List<LancamentoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    public static Lancamento toEntity(LancamentoDTO dto, Usuario usuario, ContaBancaria contaBancaria, CentroCusto centroCusto, Entidade entidade, CartaoCredito cartaoCredito) {
        if (dto == null) return null;

        Lancamento e = new Lancamento();

        e.setId(dto.getId());

        e.setDescricao(trim(dto.getDescricao()));
        e.setValor(dto.getValor());
        e.setDataCompetencia(dto.getDataCompetencia());
        e.setDataVencimento(dto.getDataVencimento());
        e.setValorBaixado(dto.getValorBaixado());
        e.setUsuario(usuario); // pode ser null se DTO não trouxer usuario
        e.setContaBancaria(contaBancaria); // pode ser null se DTO não trouxer contaBancaria
        e.setCentroCusto(centroCusto); // pode ser null se DTO não trouxer centroCusto
        e.setEntidade(entidade); // pode ser null se DTO não trouxer entidade
        e.setCartaoCredito(cartaoCredito); // pode ser null se DTO não trouxer cartaoCredito
        e.setStatusLancamento(StatusLancamento.toEnum(dto.getStatusLancamento())); // int -> enum
        e.setMeioPagamento(MeioPagamento.toEnum(dto.getMeioPagamento())); // int -> enum
        e.setTipoLancamento(TipoLancamento.toEnum(dto.getTipoLancamento())); // int -> enum

        return e;
    }
    public static Lancamento toEntity(LancamentoDTO dto, Function<Integer, Usuario> usuarioResolver, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, CentroCusto> centroCustoResolver, Function<Integer, Entidade> entidadeResolver, Function<Integer, CartaoCredito> cartaoCreditoResolver) {
        if (dto == null) return null;
        Usuario usuario = (dto.getUsuarioId() == null) ? null : usuarioResolver.apply(dto.getUsuarioId());
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        CentroCusto centroCusto = (dto.getCentroCustoId() == null) ? null : centroCustoResolver.apply(dto.getCentroCustoId());
        Entidade entidade = (dto.getEntidadeId() == null) ? null : entidadeResolver.apply(dto.getEntidadeId());
        CartaoCredito cartaoCredito = (dto.getCartaoCreditoId() == null) ? null : cartaoCreditoResolver.apply(dto.getCartaoCreditoId());
        return toEntity(dto, usuario, contaBancaria, centroCusto, entidade, cartaoCredito);
    }
    public static void copyToEntity(LancamentoDTO dto, Lancamento target, Usuario usuario, ContaBancaria contaBancaria, CentroCusto centroCusto, Entidade entidade, CartaoCredito cartaoCredito) {
        if (dto == null || target == null) return;

        target.setDescricao(trim(dto.getDescricao()));
        target.setValor(dto.getValor());
        target.setDataCompetencia(dto.getDataCompetencia());
        target.setDataVencimento(dto.getDataVencimento());
        target.setValorBaixado(dto.getValorBaixado());
        target.setUsuario(usuario);
        target.setContaBancaria(contaBancaria);
        target.setCentroCusto(centroCusto);
        target.setEntidade(entidade);
        target.setCartaoCredito(cartaoCredito);
        target.setStatusLancamento(StatusLancamento.toEnum(dto.getStatusLancamento()));
        target.setMeioPagamento(MeioPagamento.toEnum(dto.getMeioPagamento()));
        target.setTipoLancamento(TipoLancamento.toEnum(dto.getTipoLancamento()));
    }
    public static void copyToEntity(LancamentoDTO dto, Lancamento target, Function<Integer, Usuario> usuarioResolver, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, CentroCusto> centroCustoResolver, Function<Integer, Entidade> entidadeResolver, Function<Integer, CartaoCredito> cartaoCreditoResolver) {
        if (dto == null || target == null) return;
        Usuario usuario = (dto.getUsuarioId() == null) ? null : usuarioResolver.apply(dto.getUsuarioId());
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        CentroCusto centroCusto = (dto.getCentroCustoId() == null) ? null : centroCustoResolver.apply(dto.getCentroCustoId());
        Entidade entidade = (dto.getEntidadeId() == null) ? null : entidadeResolver.apply(dto.getEntidadeId());
        CartaoCredito cartaoCredito = (dto.getCartaoCreditoId() == null) ? null : cartaoCreditoResolver.apply(dto.getCartaoCreditoId());
        copyToEntity(dto, target, usuario, contaBancaria, centroCusto, entidade, cartaoCredito);
    }
    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}