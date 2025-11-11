package com.projeto.mappers;

import com.projeto.domains.Usuario;
import com.projeto.domains.CartaoCredito;
import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.domains.enums.StatusCartao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para CartaoCredito.
 * - Entity -> DTO: enum Status vira int (0/1) e Usuario vira usuarioId.
 * - DTO -> Entity: int (0/1) vira enum Status; usuarioId vira Usuario (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class CartaoCreditoMapper {


    private CartaoCreditoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static CartaoCreditoDTO toDto(CartaoCredito e) {
        if (e == null) return null;

        // idCartaoCredito (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer usuarioId = Math.toIntExact((e.getUsuario() == null) ? null : e.getUsuario().getId());
        int statusInt = (e.getStatusCartao() == null) ? 0 : e.getStatusCartao().getId();

        return new CartaoCreditoDTO(
                idDto,
                e.getBandeira(),
                e.getEmissor(),
                e.getApelido(),
                e.getFechamentoFaturaDia(),
                e.getVencimentoFaturaDia(),
                usuarioId,
                statusInt
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<CartaoCreditoDTO> toDtoList(Collection<CartaoCredito> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(CartaoCreditoMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<CartaoCreditoDTO> toDtoPage(Page<CartaoCredito> page) {
        List<CartaoCreditoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Usuario já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static CartaoCredito toEntity(CartaoCreditoDTO dto, Usuario usuario) {
        if (dto == null) return null;

        CartaoCredito e = new CartaoCredito();

        // idCartaoCredito do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setBandeira(trim(dto.getBandeira()));
        e.setEmissor(trim(dto.getEmissor()));
        e.setApelido(trim(dto.getApelido()));
        e.setFechamentoFaturaDia(dto.getFechamentoFaturaDia());
        e.setVencimentoFaturaDia(dto.getVencimentoFaturaDia());
        e.setUsuario(usuario); // pode ser null se DTO não trouxer grupo
        e.setStatusCartao(StatusCartao.toEnum(dto.getStatusCartao())); // int -> enum

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Usuario via função (repo).
     * Ex.: toEntity(dto, grupoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static CartaoCredito toEntity(CartaoCreditoDTO dto, Function<Integer, Usuario> grupoResolver) {
        if (dto == null) return null;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        return toEntity(dto, grupo);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Usuario já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(CartaoCreditoDTO dto, CartaoCredito target, Usuario usuario) {
        if (dto == null || target == null) return;

        target.setBandeira(trim(dto.getBandeira()));
        target.setEmissor(trim(dto.getEmissor()));
        target.setApelido(trim(dto.getApelido()));
        target.setFechamentoFaturaDia(dto.getFechamentoFaturaDia());
        target.setVencimentoFaturaDia(dto.getVencimentoFaturaDia());
        target.setUsuario(usuario);
        target.setStatusCartao(StatusCartao.toEnum(dto.getStatusCartao()));
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Usuario via função. Não altera o id do target.
     */
    public static void copyToEntity(CartaoCreditoDTO dto, CartaoCredito target, Function<Integer, Usuario> grupoResolver) {
        if (dto == null || target == null) return;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        copyToEntity(dto, target, grupo);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
    
}
