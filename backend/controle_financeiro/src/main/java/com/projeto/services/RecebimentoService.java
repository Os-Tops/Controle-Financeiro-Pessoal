package com.projeto.services;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.Lancamento;
import com.projeto.domains.Recebimento;
import com.projeto.domains.dtos.RecebimentoDTO;
import com.projeto.mappers.RecebimentoMapper;
import com.projeto.repositories.RecebimentoRepository;
import com.projeto.repositories.LancamentoRepository;
import com.projeto.repositories.ContaBancariaRepository;
import com.projeto.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecebimentoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final RecebimentoRepository recebimentoRepo;
    private final LancamentoRepository lancamentoRepo;
    private final ContaBancariaRepository contaBancariaRepo;

    public RecebimentoService(RecebimentoRepository recebimentoRepo,
                              LancamentoRepository lancamentoRepo,
                              ContaBancariaRepository contaBancariaRepo) {
        this.recebimentoRepo = recebimentoRepo;
        this.lancamentoRepo = lancamentoRepo;
        this.contaBancariaRepo = contaBancariaRepo;
    }

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<RecebimentoDTO> findAll() {
        return RecebimentoMapper.toDtoList(recebimentoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<RecebimentoDTO> findAll(Pageable pageable) {
        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        Page<Recebimento> page = recebimentoRepo.findAll(effective);
        return RecebimentoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por lancamento */
    @Transactional(readOnly = true)
    public Page<RecebimentoDTO> findAllByLancamento(Long lancamentoId, Pageable pageable) {
        if (lancamentoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lancamentoId é obrigatório");
        }

        // valida existência do lançamento para erro claro
        if (!lancamentoRepo.existsById(lancamentoId)) {
            throw new ObjectNotFoundException("Lancamento não encontrado: id=" + lancamentoId);
        }

        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }
        Page<Recebimento> page = recebimentoRepo.findByLancamento_Id(lancamentoId, effective);
        return RecebimentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<RecebimentoDTO> findAllByLancamento(Long lancamentoId) {
        return findAllByLancamento(lancamentoId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por contaBancaria */
    @Transactional(readOnly = true)
    public Page<RecebimentoDTO> findAllByContaBancaria(Long contaBancariaId, Pageable pageable) {
        if (contaBancariaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaBancariaId é obrigatório");
        }

        // valida existência da conta bancária para erro claro
        if (!contaBancariaRepo.existsById(contaBancariaId)) {
            throw new ObjectNotFoundException("Conta Bancaria não encontrada: id=" + contaBancariaId);
        }

        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }
        Page<Recebimento> page = recebimentoRepo.findByContaBancaria_Id(contaBancariaId, effective);
        return RecebimentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<RecebimentoDTO> findAllByContaBancaria(Long contaBancariaId) {
        return findAllByContaBancaria(contaBancariaId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public RecebimentoDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return recebimentoRepo.findById(Long.valueOf(id))
                .map(RecebimentoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Recebimento não encontrado: id=" + id));
    }

    //Create
    @Transactional
    public RecebimentoDTO create(RecebimentoDTO recebimentoDTO, Integer lancamentoId) {

        if (recebimentoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do recebimento são obrigatórios");
        }

        if (recebimentoDTO.getContaBancariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Conta de Destino é obrigatório");
        }

        ContaBancaria contaBancaria = contaBancariaRepo.findById(Long.valueOf(recebimentoDTO.getContaBancariaId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + recebimentoDTO.getContaBancariaId())
                );

        Lancamento lancamento = lancamentoRepo.findById(Long.valueOf(lancamentoId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Lançamento não encontrado: id=" + recebimentoDTO.getLancamentoId())
                );

        recebimentoDTO.setId(null);
        recebimentoDTO.setLancamentoId(lancamentoId);
        Recebimento recebimento;
        try{
            recebimento = RecebimentoMapper.toEntity(recebimentoDTO, contaBancaria, lancamento);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return RecebimentoMapper.toDto(recebimentoRepo.save(recebimento));
    }
    
}