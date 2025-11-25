package com.projeto.services;

import com.projeto.domains.Lancamento;
import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import com.projeto.mappers.LancamentoMapper;
import com.projeto.repositories.*;
import com.projeto.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LancamentoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final LancamentoRepository lancamentoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ContaBancariaRepository contaBancariaRepo;
    private final CentroCustoRepository  centroCustoRepo;
    private final EntidadeRepository entidadeRepo;
    private final CartaoCreditoRepository cartaoCreditoRepo;

    public LancamentoService(LancamentoRepository lancamentoRepo,
                             UsuarioRepository usuarioRepo,
                             ContaBancariaRepository contaBancariaRepo,
                             CentroCustoRepository centroCustoRepo,
                             EntidadeRepository entidadeRepo,
                             CartaoCreditoRepository cartaoCreditoRepo) {
        this.lancamentoRepo = lancamentoRepo;
        this.usuarioRepo = usuarioRepo;
        this.contaBancariaRepo = contaBancariaRepo;
        this.centroCustoRepo = centroCustoRepo;
        this.entidadeRepo = entidadeRepo;
        this.cartaoCreditoRepo = cartaoCreditoRepo;
    }

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAll() {
        return LancamentoMapper.toDtoList(lancamentoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAll(Pageable pageable) {
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

        Page<Lancamento> page = lancamentoRepo.findAll(effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por usuario */
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId é obrigatório");
        }

        // valida existência do usuário para erro claro
        if (!usuarioRepo.existsById(Long.valueOf(usuarioId))) {
            throw new ObjectNotFoundException("Usuario não encontrado: id=" + usuarioId);
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
        Page<Lancamento> page = lancamentoRepo.findByUsuario_Id(usuarioId, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por usuario (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por ContaBancaria*/
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByContaBancaria(Integer contaBancariaId, Pageable pageable) {
        if (contaBancariaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaBancariaId é obrigatório");
        }

        // valida existência da conta bancária para erro claro
        if (!contaBancariaRepo.existsById(Long.valueOf(contaBancariaId))) {
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
        Page<Lancamento> page = lancamentoRepo.findByContaBancaria_Id(Long.valueOf(contaBancariaId), effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por conta bancaria (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByContaBancaria(Integer contaBancariaId) {
        return findAllByContaBancaria(contaBancariaId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por CentroCusto*/
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByCentroCusto(Integer centroCustoId, Pageable pageable) {
        if (centroCustoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "centroCustoId é obrigatório");
        }

        // valida existência da conta bancária para erro claro
        if (!centroCustoRepo.existsById(Long.valueOf(centroCustoId))) {
            throw new ObjectNotFoundException("Centro de Custo não encontrada: id=" + centroCustoId);
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
        Page<Lancamento> page = lancamentoRepo.findByCentroCusto_Id(centroCustoId, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por centro de custo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByCentroCusto(Integer centroCustoId) {
        return findAllByCentroCusto(centroCustoId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por Entidade*/
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByEntidade(Integer entidadeId, Pageable pageable) {
        if (entidadeId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "entidadeId é obrigatório");
        }

        // valida existência da conta bancária para erro claro
        if (!entidadeRepo.existsById(Long.valueOf(entidadeId))) {
            throw new ObjectNotFoundException("Entidade não encontrada: id=" + entidadeId);
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
        Page<Lancamento> page = lancamentoRepo.findByEntidade_Id(entidadeId, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por Entidade (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByEntidade(Integer entidadeId) {
        return findAllByEntidade(entidadeId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por Cartao Credito*/
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByCartaoCredito(Integer cartaoCreditoId, Pageable pageable) {
        if (cartaoCreditoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cartaoCreditoId é obrigatório");
        }

        // valida existência da conta bancária para erro claro
        if (!cartaoCreditoRepo.existsById(Long.valueOf(cartaoCreditoId))) {
            throw new ObjectNotFoundException("Cartão de Credito não encontrada: id=" + cartaoCreditoId);
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
        Page<Lancamento> page = lancamentoRepo.findByCartaoCredito_Id(cartaoCreditoId, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por Cartao Credito (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByCartaoCredito(Integer cartaoCreditoId) {
        return findAllByCartaoCredito(cartaoCreditoId, Pageable.unpaged()).getContent();
    }


}