package com.projeto.mappers;

import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.UsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UsuarioMapper {

    private UsuarioMapper() {}

    /** Converte uma Entity em DTO. */
    public static UsuarioDTO toDto(Usuario e) {
        if (e == null) return null;
        return new UsuarioDTO(
                e.getId(),
                e.getNome(),
                e.getEmail(),
                e.getCriadoEm()
        );
    }

    /** Cria uma nova Entity a partir do DTO (respeita id do DTO se presente). */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        Usuario e = new Usuario();
        e.setId(dto.getId()); // se null, JPA gera; se não, usado no update
        e.setNome(dto.getNome() == null ? null : dto.getNome().trim());
        e.setEmail(dto.getEmail() == null ? null : dto.getEmail().trim());
        return e;
    }

    /**
     * Copia dados do DTO para uma Entity existente (PUT “completo”).
     * Não altera o id da entidade alvo.
     */
    public static void copyToEntity(UsuarioDTO dto, Usuario target) {
        if (dto == null || target == null) return;
        target.setNome(dto.getNome() == null ? null : dto.getNome().trim());
        target.setEmail(dto.getEmail() == null ? null : dto.getEmail().trim());
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<UsuarioDTO> toDtoList(Collection<Usuario> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte uma coleção de DTOs em lista de Entities. */
    public static List<Usuario> toEntityList(Collection<UsuarioDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(UsuarioMapper::toEntity)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> (preserva paginação). */
    public static Page<UsuarioDTO> toDtoPage(Page<Usuario> page) {
        List<UsuarioDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    
}
