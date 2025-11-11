package com.projeto.mappers;

import com.projeto.domains.Usuario;
import com.projeto.domains.CentroCusto;
import com.projeto.domains.dtos.CentroCustoDTO;
import com.projeto.domains.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para CentroCusto.
 * - Entity -> DTO: enum Status vira int (0/1) e Usuario vira usuarioId.
 * - DTO -> Entity: int (0/1) vira enum Status; usuarioId vira Usuario (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class CentroCustoMapper {

    private CentroCustoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static CentroCustoDTO toDto(CentroCusto e) {
        if (e == null) return null;

        // idCentroCusto (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer usuarioId = Math.toIntExact((e.getUsuario() == null) ? null : e.getUsuario().getId());
        int statusInt = (e.getStatus() == null) ? 0 : e.getStatus().getId();

        return new CentroCustoDTO(
                idDto,
                e.getNome(),
                e.getCodigo(),
                usuarioId,
                statusInt
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<CentroCustoDTO> toDtoList(Collection<CentroCusto> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(CentroCustoMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<CentroCustoDTO> toDtoPage(Page<CentroCusto> page) {
        List<CentroCustoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Usuario já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static CentroCusto toEntity(CentroCustoDTO dto, Usuario usuario) {
        if (dto == null) return null;

        CentroCusto e = new CentroCusto();

        // idCentroCusto do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setNome(trim(dto.getNome()));
        e.setCodigo(dto.getCodigo());
        e.setUsuario(usuario); // pode ser null se DTO não trouxer grupo
        e.setStatus(Status.toEnum(dto.getStatus())); // int -> enum

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Usuario via função (repo).
     * Ex.: toEntity(dto, grupoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static CentroCusto toEntity(CentroCustoDTO dto, Function<Integer, Usuario> grupoResolver) {
        if (dto == null) return null;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        return toEntity(dto, grupo);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Usuario já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(CentroCustoDTO dto, CentroCusto target, Usuario usuario) {
        if (dto == null || target == null) return;

        target.setNome(trim(dto.getNome()));
        target.setCodigo(dto.getCodigo());
        target.setUsuario(usuario);
        target.setStatus(Status.toEnum(dto.getStatus()));
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Usuario via função. Não altera o id do target.
     */
    public static void copyToEntity(CentroCustoDTO dto, CentroCusto target, Function<Integer, Usuario> grupoResolver) {
        if (dto == null || target == null) return;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        copyToEntity(dto, target, grupo);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}
