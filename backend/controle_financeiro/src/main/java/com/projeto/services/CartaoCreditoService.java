package com.projeto.services;

import com.projeto.domains.CartaoCredito;
import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.mappers.CartaoCreditoMapper;
import com.projeto.repositories.CartaoCreditoRepository;
import com.projeto.repositories.UsuarioRepository;
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

    /** Paginado, filtrando por usuário */
    @Transactional(readOnly = true)
    public Page<CartaoCreditoDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId é obrigatório");
        }

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

        Page<CartaoCredito> page = cartaoCreditoRepo.findByUsuario_Id(usuarioId, effective);
        return CartaoCreditoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por usuário */
    @Transactional(readOnly = true)
    public List<CartaoCreditoDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public CartaoCreditoDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id de cartão de crédito é obrigatório");
        }

        return cartaoCreditoRepo.findById(id)
                .map(CartaoCreditoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Cartão de crédito não encontrado: id=" + id));
    }

    /* =================== CREATE =================== */

    @Transactional
    public CartaoCreditoDTO create(CartaoCreditoDTO cartaoCreditoDTO) {

        if (cartaoCreditoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do cartão de crédito são obrigatórios");
        }

        if (cartaoCreditoDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        Integer usuarioId = cartaoCreditoDTO.getUsuarioId();
        Usuario usuario = usuarioRepo.findById(Long.valueOf(usuarioId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + usuarioId)
                );

        cartaoCreditoDTO.setId(null);

        // se não vier statusCartao, default = 1 (BLOQUEADO)
        if (cartaoCreditoDTO.getStatusCartao() == null) {
            cartaoCreditoDTO.setStatusCartao(1); // mapper converte 0 -> Status.values()[0]
        }

        CartaoCredito cartaoCredito;
        try {
            cartaoCredito = CartaoCreditoMapper.toEntity(cartaoCreditoDTO, usuario);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return CartaoCreditoMapper.toDto(cartaoCreditoRepo.save(cartaoCredito));
    }

    /* =================== UPDATE =================== */

    @Transactional
    public CartaoCreditoDTO update(Long id, CartaoCreditoDTO cartaoCreditoDTO) {

        if (cartaoCreditoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do cartão de crédito são obrigatórios");
        }

        if (cartaoCreditoDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        // garante que o cartão existe
        CartaoCredito existente = cartaoCreditoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Cartão não encontrado: id=" + id));

        Integer usuarioId = cartaoCreditoDTO.getUsuarioId();
        Usuario usuario = usuarioRepo.findById(Long.valueOf(usuarioId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + usuarioId)
                );

        cartaoCreditoDTO.setId(id);

        if (cartaoCreditoDTO.getStatusCartao() == null && existente.getStatusCartao() != null) {
        }

        CartaoCredito cartaoCredito;
        try {
            cartaoCredito = CartaoCreditoMapper.toEntity(cartaoCreditoDTO, usuario);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return CartaoCreditoMapper.toDto(cartaoCreditoRepo.save(cartaoCredito));
    }

    /* =================== DELETE =================== */

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        CartaoCredito cartaoCredito = cartaoCreditoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Cartão de crédito não encontrado: id=" + id));

        cartaoCreditoRepo.delete(cartaoCredito);
    }
}
