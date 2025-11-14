package com.projeto.mappers;

import com.projeto.domains.Transferencia;
import com.projeto.domains.dtos.TransferenciaDTO;
import com.projeto.domains.ContaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransferenciaMapper {

    private TransferenciaMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static TransferenciaDTO toDto(Transferencia e) {
        if (e == null) return null;

        Long idDto = e.getId();

        // conversão segura para Integer (evita NPE)
        Long contaOrigemIdLong  = (e.getContaOrigem()  == null) ? null : e.getContaOrigem().getId();
        Long contaDestinoIdLong = (e.getContaDestino() == null) ? null : e.getContaDestino().getId();

        Integer contaOrigemId  = (contaOrigemIdLong  == null) ? null : Math.toIntExact(contaOrigemIdLong);
        Integer contaDestinoId = (contaDestinoIdLong == null) ? null : Math.toIntExact(contaDestinoIdLong);

        return new TransferenciaDTO(
                idDto,
                contaOrigemId,
                contaDestinoId,
                e.getData(),
                e.getValor(),
                e.getObservacao()
        );
    }

    public static List<TransferenciaDTO> toDtoList(Collection<Transferencia> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(TransferenciaMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<TransferenciaDTO> toDtoPage(Page<Transferencia> page) {
        List<TransferenciaDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    // Versão completa: recebe conta de origem e conta de destino
    public static Transferencia toEntity(TransferenciaDTO dto,
                                         ContaBancaria contaOrigem,
                                         ContaBancaria contaDestino) {
        if (dto == null) return null;

        Transferencia e = new Transferencia();

        e.setId(dto.getId());
        e.setValor(dto.getValor());
        e.setObservacao(trim(dto.getObservacao()));
        e.setData(dto.getData());
        e.setContaOrigem(contaOrigem);
        e.setContaDestino(contaDestino);

        return e;
    }

    // Versão que resolve as contas a partir dos IDs do DTO
    public static Transferencia toEntity(TransferenciaDTO dto,
                                         Function<Integer, ContaBancaria> contaResolver) {
        if (dto == null) return null;

        ContaBancaria contaOrigem = (dto.getContaOrigemId()  == null) ? null : contaResolver.apply(dto.getContaOrigemId());
        ContaBancaria contaDestino = (dto.getContaDestinoId() == null) ? null : contaResolver.apply(dto.getContaDestinoId());

        return toEntity(dto, contaOrigem, contaDestino);
    }

    /* ======================= copyToEntity ======================= */

    public static void copyToEntity(TransferenciaDTO dto,
                                    Transferencia target,
                                    ContaBancaria contaOrigem,
                                    ContaBancaria contaDestino) {
        if (dto == null || target == null) return;

        target.setValor(dto.getValor());
        target.setObservacao(trim(dto.getObservacao()));
        target.setData(dto.getData());
        target.setContaOrigem(contaOrigem);
        target.setContaDestino(contaDestino);
    }

    public static void copyToEntity(TransferenciaDTO dto,
                                    Transferencia target,
                                    Function<Integer, ContaBancaria> contaResolver) {
        if (dto == null || target == null) return;

        ContaBancaria contaOrigem  = (dto.getContaOrigemId()  == null) ? null : contaResolver.apply(dto.getContaOrigemId());
        ContaBancaria contaDestino = (dto.getContaDestinoId() == null) ? null : contaResolver.apply(dto.getContaDestinoId());

        copyToEntity(dto, target, contaOrigem, contaDestino);
    }

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}
