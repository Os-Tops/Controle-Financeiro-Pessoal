package com.projeto.mappers;

import com.projeto.domains.Usuario;
import com.projeto.domains.Entidade;
import com.projeto.domains.dtos.EntidadeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Entidade.
 * - Entity → DTO: enum Status vira int (0/1) e Usuario vira usuarioId.
 * - DTO → Entity: int (0/1) vira enum Status; usuarioId vira Usuario (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class EntidadeMapper {

    private EntidadeMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static EntidadeDTO toDto(Entidade e) {
        if (e == null) return null;

        // idEntidade (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer usuarioId = Math.toIntExact((e.getUsuario() == null) ? null : e.getUsuario().getId());

        return new EntidadeDTO(
                idDto,
                e.getNome(),
                e.getDocumento(),
                usuarioId
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<EntidadeDTO> toDtoList(Collection<Entidade> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(EntidadeMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<EntidadeDTO> toDtoPage(Page<Entidade> page) {
        List<EntidadeDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Usuario já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static Entidade toEntity(EntidadeDTO dto, Usuario usuario) {
        if (dto == null) return null;

        Entidade e = new Entidade();

        // idEntidade do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setNome(trim(dto.getNome()));
        e.setDocumento(trim(dto.getDocumento()));
        e.setUsuario(usuario); // pode ser null se DTO não trouxer grupo

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Usuario via função (repo).
     * Ex.: toEntity(dto, grupoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static Entidade toEntity(EntidadeDTO dto, Function<Integer, Usuario> grupoResolver) {
        if (dto == null) return null;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        return toEntity(dto, grupo);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Usuario já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(EntidadeDTO dto, Entidade target, Usuario usuario) {
        if (dto == null || target == null) return;

        target.setNome(trim(dto.getNome()));
        target.setDocumento(trim(dto.getDocumento()));
        target.setUsuario(usuario);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Usuario via função. Não altera o id do target.
     */
    public static void copyToEntity(EntidadeDTO dto, Entidade target, Function<Integer, Usuario> grupoResolver) {
        if (dto == null || target == null) return;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        copyToEntity(dto, target, grupo);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}
