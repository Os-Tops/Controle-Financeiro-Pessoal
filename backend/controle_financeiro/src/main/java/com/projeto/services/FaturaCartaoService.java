package com.projeto.services;

import com.projeto.domains.FaturaCartao;
import com.projeto.domains.dtos.FaturaCartaoDTO;
import com.projeto.mappers.FaturaCartaoMapper;
import com.projeto.repositories.CartaoCreditoRepository;
import com.projeto.repositories.FaturaCartaoRepository;
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
public class FaturaCartaoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final FaturaCartaoRepository faturaCartaoRepo;
    private final CartaoCreditoRepository cartaoCreditoRepo;

    public FaturaCartaoService(FaturaCartaoRepository faturaCartaoRepo,
                              CartaoCreditoRepository cartaoCreditoRepo) {
        this.faturaCartaoRepo = faturaCartaoRepo;
        this.cartaoCreditoRepo = cartaoCreditoRepo;
    }

    /* =================== READ =================== */

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<FaturaCartaoDTO> findAll() {
        return FaturaCartaoMapper.toDtoList(faturaCartaoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<FaturaCartaoDTO> findAll(Pageable pageable) {
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

        Page<FaturaCartao> page = faturaCartaoRepo.findAll(effective);
        return FaturaCartaoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por grupo */
    @Transactional(readOnly = true)
    public Page<FaturaCartaoDTO> findAllByCartao(Integer cartaoCreditoId, Pageable pageable) {
        if (cartaoCreditoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cartaoCreditoId é obrigatório");
        }

        // valida existência do grupo para erro claro
        if (!cartaoCreditoRepo.existsById(Long.valueOf(cartaoCreditoId))) {
            throw new ObjectNotFoundException("CartaoCredito não encontrado: id=" + cartaoCreditoId);
        }

        // ✅ trate unpaged aqui
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

        Page<FaturaCartao> page = faturaCartaoRepo.findByCartaoCredito_Id(cartaoCreditoId, effective);
        return FaturaCartaoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por grupo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<FaturaCartaoDTO> findAllByCartao(Integer cartaoCreditoId) {
        return findAllByCartao(cartaoCreditoId, Pageable.unpaged()).getContent();
    }
    
}
