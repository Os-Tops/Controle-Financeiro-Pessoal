package com.projeto.services;

import com.projeto.domains.CartaoCredito;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.mappers.CartaoCreditoMapper;
import com.projeto.mappers.ContaBancariaMapper;
import com.projeto.repositories.CartaoCreditoRepository;
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

import java.util.List;

@Service
public class ContaBancariaService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final ContaBancariaRepository contaBancariaRepo;
    private final UsuarioRepository usuarioRepo;

    public ContaBancariaService(ContaBancariaRepository contaBancariaRepo,
                                UsuarioRepository usuarioRepo) {
        this.contaBancariaRepo = contaBancariaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<ContaBancariaDTO> findAll() {
        return ContaBancariaMapper.toDtoList(contaBancariaRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> findAll(Pageable pageable) {
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

        Page<ContaBancaria> page = contaBancariaRepo.findAll(effective);
        return ContaBancariaMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId é obrigatório");
        }

        // valida existência do grupo para erro claro
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
        Page<ContaBancaria> page = contaBancariaRepo.findByUsuario_Id(usuarioId, effective);
        return ContaBancariaMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<ContaBancariaDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }
}
