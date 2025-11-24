package com.projeto.services;

import com.projeto.domains.Lancamento;
import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import com.projeto.mappers.LancamentoMapper;
import com.projeto.repositories.LancamentoRepository;
import com.projeto.repositories.UsuarioRepository;
import com.projeto.repositories.ContaBancariaRepository;
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

    public LancamentoService(LancamentoRepository lancamentoRepo,
                             UsuarioRepository usuarioRepo,
                             ContaBancariaRepository contaBancariaRepo) {
        this.lancamentoRepo = lancamentoRepo;
        this.usuarioRepo = usuarioRepo;
        this.contaBancariaRepo = contaBancariaRepo;
    }

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

    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByContaBancaria(Long contaBancariaId, Pageable pageable) {
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
        Page<Lancamento> page = lancamentoRepo.findByContaBancaria_Id(contaBancariaId, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByStatus(StatusLancamento status, Pageable pageable) {
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status é obrigatório");
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
        Page<Lancamento> page = lancamentoRepo.findByStatusLancamento(status, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByTipo(TipoLancamento tipo, Pageable pageable) {
        if (tipo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipo é obrigatório");
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
        Page<Lancamento> page = lancamentoRepo.findByTipoLancamento(tipo, effective);
        return LancamentoMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAllByDataVencimento(LocalDate inicio, LocalDate fim, Pageable pageable) {
        if (inicio == null || fim == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "início e fim são obrigatórios");
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
        Page<Lancamento> page = lancamentoRepo.findByDataVencimentoBetween(inicio, fim, effective);
        return LancamentoMapper.toDtoPage(page);
    }
}