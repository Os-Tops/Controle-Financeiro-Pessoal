package com.projeto.mappers;

import com.projeto.domains.Usuario;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.domains.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContaBancariaMapper {

    private ContaBancariaMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static ContaBancariaDTO toDto(ContaBancaria e) {
        if (e == null) return null;

        Long idDto = e.getId();

        Long usuarioId = (e.getUsuario() == null) ? null : e.getUsuario().getId();
        int statusInt = (e.getStatus() == null) ? 0 : e.getStatus().getId();

        return new ContaBancariaDTO(
                idDto,
                e.getInstituicao(),
                e.getAgencia(),
                e.getNumero(),
                e.getApelido(),
                e.getSaldoInicial(),
                e.getDataSaldoInicial(),
                statusInt,
                usuarioId
        );
    }
    public static List<ContaBancariaDTO> toDtoList(Collection<ContaBancaria> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(ContaBancariaMapper::toDto)
                .collect(Collectors.toList());
    }
    public static Page<ContaBancariaDTO> toDtoPage(Page<ContaBancaria> page) {
        List<ContaBancariaDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    public static ContaBancaria toEntity(ContaBancariaDTO dto, Usuario usuario) {
        if (dto == null) return null;

        ContaBancaria e = new ContaBancaria();

        e.setId(dto.getId());

        e.setAgencia(dto.getAgencia());
        e.setApelido(trim(dto.getApelido()));
        e.setInstituicao(trim(dto.getInstituicao()));
        e.setNumero(dto.getNumero());
        e.setSaldoInicial(dto.getSaldoInicial());
        e.setDataSaldoInicial(dto.getDataSaldoInicial());
        e.setUsuario(usuario); // pode ser null se DTO não trouxer grupo
        e.setStatus(Status.toEnum(dto.getStatus())); // int -> enum

        return e;
    }
    public static ContaBancaria toEntity(ContaBancariaDTO dto, Function<Long, Usuario> grupoResolver) {
        if (dto == null) return null;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        return toEntity(dto, grupo);
    }
    public static void copyToEntity(ContaBancariaDTO dto, ContaBancaria target, Usuario usuario) {
        if (dto == null || target == null) return;

        target.setAgencia(dto.getAgencia());
        target.setApelido(trim(dto.getApelido()));
        target.setInstituicao(trim(dto.getInstituicao()));
        target.setNumero(dto.getNumero());
        target.setSaldoInicial(dto.getSaldoInicial());
        target.setDataSaldoInicial(dto.getDataSaldoInicial());
        target.setUsuario(usuario);
        target.setStatus(Status.toEnum(dto.getStatus()));
    }
    public static void copyToEntity(ContaBancariaDTO dto, ContaBancaria target, Function<Long, Usuario> grupoResolver) {
        if (dto == null || target == null) return;
        Usuario grupo = (dto.getUsuarioId() == null) ? null : grupoResolver.apply(dto.getUsuarioId());
        copyToEntity(dto, target, grupo);
    }
    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
    }
