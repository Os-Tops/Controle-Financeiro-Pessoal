package com.projeto.services;

import com.projeto.domains.Pagamento;
import com.projeto.domains.dtos.PagamentoDTO;
import com.projeto.mappers.PagamentoMapper;
import com.projeto.repositories.PagamentoRepository;
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
public class PagamentoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final PagamentoRepository pagamentoRepo;
    private final LancamentoRepository lancamentoRepo;
    private final ContaBancariaRepository contaBancariaRepo;

    public PagamentoService(PagamentoRepository pagamentoRepo,
                            LancamentoRepository lancamentoRepo,
                            ContaBancariaRepository contaBancariaRepo) {
        this.pagamentoRepo = pagamentoRepo;
        this.lancamentoRepo = lancamentoRepo;
        this.contaBancariaRepo = contaBancariaRepo;
    }

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAll() {
        return PagamentoMapper.toDtoList(pagamentoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<PagamentoDTO> findAll(Pageable pageable) {
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

        Page<Pagamento> page = pagamentoRepo.findAll(effective);
        return PagamentoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por lancamento */
    @Transactional(readOnly = true)
    public Page<PagamentoDTO> findAllByLancamento(Long lancamentoId, Pageable pageable) {
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
        Page<Pagamento> page = pagamentoRepo.findByLancamento_Id(lancamentoId, effective);
        return PagamentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllByLancamento(Long lancamentoId) {
        return findAllByLancamento(lancamentoId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por contaBancaria */
    @Transactional(readOnly = true)
    public Page<PagamentoDTO> findAllByContaBancaria(Long contaBancariaId, Pageable pageable) {
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
        Page<Pagamento> page = pagamentoRepo.findByContaBancaria_Id(contaBancariaId, effective);
        return PagamentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllByContaBancaria(Long contaBancariaId) {
        return findAllByContaBancaria(contaBancariaId, Pageable.unpaged()).getContent();
    }
}