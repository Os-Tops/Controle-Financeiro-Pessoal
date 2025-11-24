package com.projeto.mappers;

import com.projeto.domains.Recebimento;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.Lancamento;
import com.projeto.domains.dtos.RecebimentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecebimentoMapper {

    private RecebimentoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static RecebimentoDTO toDto(Recebimento e) {
        if (e == null) return null;

        Long idDto = e.getId();

        Integer contaBancariaId = Math.toIntExact((e.getContaBancaria() == null) ? null : e.getContaBancaria().getId());
        Integer lancamentoId = Math.toIntExact((e.getLancamento() == null) ? null : e.getLancamento().getId());

        return new RecebimentoDTO(
                idDto,
                e.getDataRecebimento(),
                e.getValorRecebido(),
                e.getObservacao(),
                contaBancariaId,
                lancamentoId
        );
    }
    public static List<RecebimentoDTO> toDtoList(Collection<Recebimento> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(RecebimentoMapper::toDto)
                .collect(Collectors.toList());
    }
    public static Page<RecebimentoDTO> toDtoPage(Page<Recebimento> page) {
        List<RecebimentoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    public static Recebimento toEntity(RecebimentoDTO dto, ContaBancaria contaBancaria, Lancamento lancamento) {
        if (dto == null) return null;

        Recebimento e = new Recebimento();

        e.setId(dto.getId());

        e.setDataRecebimento(dto.getDataRecebimento());
        e.setValorRecebido(dto.getValorRecebido());
        e.setObservacao(trim(dto.getObservacao()));
        e.setContaBancaria(contaBancaria); // pode ser null se DTO não trouxer contaBancaria
        e.setLancamento(lancamento); // pode ser null se DTO não trouxer lancamento

        return e;
    }
    public static Recebimento toEntity(RecebimentoDTO dto, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, Lancamento> lancamentoResolver) {
        if (dto == null) return null;
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        Lancamento lancamento = (dto.getLancamentoId() == null) ? null : lancamentoResolver.apply(dto.getLancamentoId());
        return toEntity(dto, contaBancaria, lancamento);
    }
    public static void copyToEntity(RecebimentoDTO dto, Recebimento target, ContaBancaria contaBancaria, Lancamento lancamento) {
        if (dto == null || target == null) return;

        target.setDataRecebimento(dto.getDataRecebimento());
        target.setValorRecebido(dto.getValorRecebido());
        target.setObservacao(trim(dto.getObservacao()));
        target.setContaBancaria(contaBancaria);
        target.setLancamento(lancamento);
    }
    public static void copyToEntity(RecebimentoDTO dto, Recebimento target, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, Lancamento> lancamentoResolver) {
        if (dto == null || target == null) return;
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        Lancamento lancamento = (dto.getLancamentoId() == null) ? null : lancamentoResolver.apply(dto.getLancamentoId());
        copyToEntity(dto, target, contaBancaria, lancamento);
    }
    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}