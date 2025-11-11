package com.projeto.mappers;

import com.projeto.domains.CartaoCredito;
import com.projeto.domains.FaturaCartao;
import com.projeto.domains.dtos.FaturaCartaoDTO;
import com.projeto.domains.enums.Status;
import com.projeto.domains.enums.StatusFatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para FaturaCartao.
 * - Entity -> DTO: enum Status vira int (0/1) e CartaoCredito vira cartaoCreditoId.
 * - DTO -> Entity: int (0/1) vira enum Status; cartaoCreditoId vira CartaoCredito (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class FaturaCartaoMapper {

    private FaturaCartaoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static FaturaCartaoDTO toDto(FaturaCartao e) {
        if (e == null) return null;

        // idFaturaCartao (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer cartaoCreditoId = Math.toIntExact((e.getCartaoCredito() == null) ? null : e.getCartaoCredito().getId());
        int statusInt = (e.getStatusFatura() == null) ? 0 : e.getStatusFatura().getId();

        return new FaturaCartaoDTO(
                idDto,
                e.getCompetencia(),
                e.getDataFechamento(),
                e.getDataVencimento(),
                e.getValorTotal(),
                cartaoCreditoId,
                statusInt
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<FaturaCartaoDTO> toDtoList(Collection<FaturaCartao> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(FaturaCartaoMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<FaturaCartaoDTO> toDtoPage(Page<FaturaCartao> page) {
        List<FaturaCartaoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o CartaoCredito já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static FaturaCartao toEntity(FaturaCartaoDTO dto, CartaoCredito cartaoCredito) {
        if (dto == null) return null;

        FaturaCartao e = new FaturaCartao();

        // idFaturaCartao do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setCompetencia(dto.getCompetencia());
        e.setDataFechamento(dto.getDataFechamentoFatura());
        e.setDataVencimento(dto.getDataVencimentoFatura());
        e.setCartaoCredito(cartaoCredito); // pode ser null se DTO não trouxer grupo
        e.setStatusFatura(StatusFatura.toEnum(dto.getStatusFatura())); // int -> enum

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o CartaoCredito via função (repo).
     * Ex.: toEntity(dto, grupoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static FaturaCartao toEntity(FaturaCartaoDTO dto, Function<Integer, CartaoCredito> grupoResolver) {
        if (dto == null) return null;
        CartaoCredito grupo = (dto.getCartaoCreditoId() == null) ? null : grupoResolver.apply(dto.getCartaoCreditoId());
        return toEntity(dto, grupo);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o CartaoCredito já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(FaturaCartaoDTO dto, FaturaCartao target, CartaoCredito cartaoCredito) {
        if (dto == null || target == null) return;

        target.setCompetencia(dto.getCompetencia());
        target.setDataFechamento(dto.getDataFechamentoFatura());
        target.setDataVencimento(dto.getDataVencimentoFatura());
        target.setCartaoCredito(cartaoCredito);
        target.setStatusFatura(StatusFatura.toEnum(dto.getStatusFatura()));
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o CartaoCredito via função. Não altera o id do target.
     */
    public static void copyToEntity(FaturaCartaoDTO dto, FaturaCartao target, Function<Integer, CartaoCredito> grupoResolver) {
        if (dto == null || target == null) return;
        CartaoCredito grupo = (dto.getCartaoCreditoId() == null) ? null : grupoResolver.apply(dto.getCartaoCreditoId());
        copyToEntity(dto, target, grupo);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
    
}
