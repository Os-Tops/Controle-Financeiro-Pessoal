package com.projeto.mappers;

import com.projeto.domains.MovimentoConta;
import com.projeto.domains.dtos.MovimentoContaDTO;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MovimentoContaMapper {

    private MovimentoContaMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static MovimentoContaDTO toDto(MovimentoConta e) {
        if (e == null) return null;

        Long idDto = e.getId();

        Long contaId = (e.getContaBancaria() == null)
                ? null
                : e.getContaBancaria().getId();

        int statusInt = (e.getTipoTransacao() == null) ? 0 : e.getTipoTransacao().getId();

        return new MovimentoContaDTO(
                idDto,
                e.getDataMovimento(),
                e.getValor(),
                e.getHistorico(),// <<-- agora é Long
                statusInt,
                contaId
                );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<MovimentoContaDTO> toDtoList(Collection<MovimentoConta> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(MovimentoContaMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<MovimentoContaDTO> toDtoPage(Page<MovimentoConta> page) {
        List<MovimentoContaDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    public static MovimentoConta toEntity(MovimentoContaDTO dto, ContaBancaria contaBancaria) {
        if (dto == null) return null;

        MovimentoConta e = new MovimentoConta();

        e.setId(dto.getId());

        e.setValor(dto.getValor());
        e.setHistorico(trim(dto.getHistorico()));
        e.setDataMovimento(dto.getDataMovimento());
        e.setContaBancaria(contaBancaria); // pode ser null se DTO não trouxer grupo
        e.setTipoTransacao(TipoTransacao.toEnum(dto.getTipoTransacao())); // int -> enum

        return e;
    }
    public static MovimentoConta toEntity(MovimentoContaDTO dto, Function<Long, ContaBancaria> contaResolver) {
        if (dto == null) return null;
        ContaBancaria conta = (dto.getContaBancariaId() == null)
                ? null
                : contaResolver.apply(dto.getContaBancariaId());
        return toEntity(dto, conta);
    }

    public static void copyToEntity(MovimentoContaDTO dto, MovimentoConta target, ContaBancaria contaBancaria) {
        if (dto == null || target == null) return;

        target.setValor(dto.getValor());
        target.setHistorico(trim(dto.getHistorico()));
        target.setDataMovimento(dto.getDataMovimento());
        target.setContaBancaria(contaBancaria); // pode ser null se DTO não trouxer grupo
        target.setTipoTransacao(TipoTransacao.toEnum(dto.getTipoTransacao()));
    }
    public static void copyToEntity(MovimentoContaDTO dto, MovimentoConta target, Function<Long, ContaBancaria> contaResolver) {
        if (dto == null || target == null) return;
        ContaBancaria conta = (dto.getContaBancariaId() == null)
                ? null
                : contaResolver.apply(dto.getContaBancariaId());
        copyToEntity(dto, target, conta);
    }
    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}
