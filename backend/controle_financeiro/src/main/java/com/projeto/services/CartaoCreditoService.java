package com.projeto.services;

import com.projeto.domains.CartaoCredito;
import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.mappers.CartaoCreditoMapper;
import com.projeto.repositories.UsuarioRepository;
import com.projeto.repositories.CartaoCreditoRepository;
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
public class CartaoCreditoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final CartaoCreditoRepository cartaoCreditoRepo;
    private final UsuarioRepository usuarioRepo;

    public CartaoCreditoService(CartaoCreditoRepository cartaoCreditoRepo,
                              UsuarioRepository usuarioRepo) {
        this.cartaoCreditoRepo = cartaoCreditoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /* =================== READ =================== */

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<CartaoCreditoDTO> findAll() {
        return CartaoCreditoMapper.toDtoList(cartaoCreditoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<CartaoCreditoDTO> findAll(Pageable pageable) {
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

        Page<CartaoCredito> page = cartaoCreditoRepo.findAll(effective);
        return CartaoCreditoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por grupo */
    @Transactional(readOnly = true)
    public Page<CartaoCreditoDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId é obrigatório");
        }

        // valida existência do grupo para erro claro
        if (!usuarioRepo.existsById(Long.valueOf(usuarioId))) {
            throw new ObjectNotFoundException("Usuario não encontrado: id=" + usuarioId);
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

        Page<CartaoCredito> page = cartaoCreditoRepo.findByUsuario_Id(usuarioId, effective);
        return CartaoCreditoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por grupo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<CartaoCreditoDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }
    
}
