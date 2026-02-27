package com.projeto.mappers;

import com.projeto.domains.TelefoneUsuario;
import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.TelefoneUsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TelefoneUsuarioMapper {

    private TelefoneUsuarioMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static TelefoneUsuarioDTO toDto(TelefoneUsuario e) {
        if (e == null) return null;

        // idTelefoneUsuario (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer usuarioId = Math.toIntExact((e.getUsuario() == null) ? null : e.getUsuario().getId());

        return new TelefoneUsuarioDTO(
                idDto,
                e.getDdd(),
                e.getNumero(),
                usuarioId
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<TelefoneUsuarioDTO> toDtoList(Collection<TelefoneUsuario> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(TelefoneUsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<TelefoneUsuarioDTO> toDtoPage(Page<TelefoneUsuario> page) {
        List<TelefoneUsuarioDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Usuario já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static TelefoneUsuario toEntity(TelefoneUsuarioDTO dto, Usuario usuario) {
        if (dto == null) return null;

        TelefoneUsuario e = new TelefoneUsuario();

        // idTelefoneUsuario do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setDdd(trim(dto.getDdd()));
        e.setNumero(trim(dto.getNumero()));
        e.setUsuario(usuario); // pode ser null se DTO não trouxer grupo

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Usuario via função (repo).
     * Ex.: toEntity(dto, grupoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static TelefoneUsuario toEntity(TelefoneUsuarioDTO dto, Function<Integer, Usuario> grupoResolver) {
        if (dto == null) return null;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        return toEntity(dto, grupo);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Usuario já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(TelefoneUsuarioDTO dto, TelefoneUsuario target, Usuario usuario) {
        if (dto == null || target == null) return;

        target.setDdd(trim(dto.getDdd()));
        target.setNumero(trim(dto.getNumero()));
        target.setUsuario(usuario);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Usuario via função. Não altera o id do target.
     */
    public static void copyToEntity(TelefoneUsuarioDTO dto, TelefoneUsuario target, Function<Integer, Usuario> grupoResolver) {
        if (dto == null || target == null) return;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        copyToEntity(dto, target, grupo);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}
