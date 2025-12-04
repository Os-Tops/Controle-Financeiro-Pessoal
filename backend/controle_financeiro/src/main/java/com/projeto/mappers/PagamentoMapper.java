package com.projeto.mappers;

import com.projeto.domains.Pagamento;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.Lancamento;
import com.projeto.domains.dtos.PagamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PagamentoMapper {

    private PagamentoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static PagamentoDTO toDto(Pagamento e) {
        if (e == null) return null;

        Long idDto = e.getId();

        Integer contaBancariaId = Math.toIntExact((e.getContaBancaria() == null) ? null : e.getContaBancaria().getId());
        Integer lancamentoId = Math.toIntExact((e.getLancamento() == null) ? null : e.getLancamento().getId());

        return new PagamentoDTO(
                idDto,
                e.getDataPagamento(),
                e.getValorPago(),
                e.getObservacao(),
                contaBancariaId,
                lancamentoId
        );
    }
    public static List<PagamentoDTO> toDtoList(Collection<Pagamento> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(PagamentoMapper::toDto)
                .collect(Collectors.toList());
    }
    public static Page<PagamentoDTO> toDtoPage(Page<Pagamento> page) {
        List<PagamentoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    public static Pagamento toEntity(PagamentoDTO dto, ContaBancaria contaBancaria, Lancamento lancamento) {
        if (dto == null) return null;

        Pagamento e = new Pagamento();

        e.setId(dto.getId());

        e.setDataPagamento(dto.getDataPagamento());
        e.setValorPago(dto.getValorPago());
        e.setObservacao(trim(dto.getObservacao()));
        e.setContaBancaria(contaBancaria); // pode ser null se DTO não trouxer contaBancaria
        e.setLancamento(lancamento); // pode ser null se DTO não trouxer lancamento

        return e;
    }
    public static Pagamento toEntity(PagamentoDTO dto, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, Lancamento> lancamentoResolver) {
        if (dto == null) return null;
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        Lancamento lancamento = (dto.getLancamentoId() == null) ? null : lancamentoResolver.apply(dto.getLancamentoId());
        return toEntity(dto, contaBancaria, lancamento);
    }
    public static void copyToEntity(PagamentoDTO dto, Pagamento target, ContaBancaria contaBancaria, Lancamento lancamento) {
        if (dto == null || target == null) return;

        target.setDataPagamento(dto.getDataPagamento());
        target.setValorPago(dto.getValorPago());
        target.setObservacao(trim(dto.getObservacao()));
        target.setContaBancaria(contaBancaria);
        target.setLancamento(lancamento);
    }
    public static void copyToEntity(PagamentoDTO dto, Pagamento target, Function<Integer, ContaBancaria> contaBancariaResolver, Function<Integer, Lancamento> lancamentoResolver) {
        if (dto == null || target == null) return;
        ContaBancaria contaBancaria = (dto.getContaBancariaId() == null) ? null : contaBancariaResolver.apply(dto.getContaBancariaId());
        Lancamento lancamento = (dto.getLancamentoId() == null) ? null : lancamentoResolver.apply(dto.getLancamentoId());
        copyToEntity(dto, target, contaBancaria, lancamento);
    }
    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}