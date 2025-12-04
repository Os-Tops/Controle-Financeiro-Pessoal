package com.projeto.services;

import com.projeto.domains.CentroCusto;
import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.CentroCustoDTO;
import com.projeto.domains.enums.Status;
import com.projeto.mappers.CentroCustoMapper;
import com.projeto.repositories.CentroCustoRepository;
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
public class CentroCustoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final CentroCustoRepository centroCustoRepo;
    private final UsuarioRepository usuarioRepo;

    public CentroCustoService(CentroCustoRepository centroCustoRepo,
                              UsuarioRepository usuarioRepo) {
        this.centroCustoRepo = centroCustoRepo;
        this.usuarioRepo = usuarioRepo;
    }


    private Status statusFromAtivo(Boolean ativo) {
        Status[] valores = Status.values();
        if (valores.length < 2) {
            throw new IllegalStateException("Enum Status precisa ter pelo menos 2 valores para mapear ATIVO/INATIVO.");
        }
        return Boolean.TRUE.equals(ativo) ? valores[0] : valores[1];
    }


    private Status statusInativo() {
        Status[] valores = Status.values();
        if (valores.length < 2) {
            throw new IllegalStateException("Enum Status precisa ter pelo menos 2 valores para mapear INATIVO.");
        }
        return valores[1];
    }


    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<CentroCustoDTO> findAll() {
        return CentroCustoMapper.toDtoList(centroCustoRepo.findAll());
    }

    /** Paginado, sem filtro */
    @Transactional(readOnly = true)
    public Page<CentroCustoDTO> findAll(Pageable pageable) {
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

        Page<CentroCusto> page = centroCustoRepo.findAll(effective);
        return CentroCustoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por usuário (se ainda usar) */
    @Transactional(readOnly = true)
    public Page<CentroCustoDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
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

        Page<CentroCusto> page = centroCustoRepo.findByUsuario_Id(usuarioId, effective);
        return CentroCustoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por usuário */
    @Transactional(readOnly = true)
    public List<CentroCustoDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por ativo (mapeado para Status via helper) */
    @Transactional(readOnly = true)
    public Page<CentroCustoDTO> findAllByAtivo(Boolean ativo, Pageable pageable) {
        if (ativo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetro 'ativo' é obrigatório");
        }

        Status statusDesejado = statusFromAtivo(ativo);

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

        Page<CentroCusto> page = centroCustoRepo.findByStatus(statusDesejado, effective);
        return CentroCustoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por ativo */
    @Transactional(readOnly = true)
    public List<CentroCustoDTO> findAllByAtivo(Boolean ativo) {
        if (ativo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetro 'ativo' é obrigatório");
        }

        Status statusDesejado = statusFromAtivo(ativo);

        return CentroCustoMapper.toDtoList(
                centroCustoRepo.findByStatus(statusDesejado)
        );
    }

    @Transactional(readOnly = true)
    public CentroCustoDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id do Centro de Custo é obrigatório");
        }

        return centroCustoRepo.findById(id)
                .map(CentroCustoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Centro de Custo não encontrado: id=" + id));
    }


    @Transactional
    public CentroCustoDTO create(CentroCustoDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do centro de custo são obrigatórios");
        }

        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        Integer usuarioId = dto.getUsuarioId();
        Usuario usuario = usuarioRepo.findById(Long.valueOf(usuarioId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + usuarioId)
                );

        dto.setId(null);

        if (dto.getStatus() == null) {
            dto.setStatus(0); // o mapper converte 0 -> Status.values()[0]
        }

        CentroCusto centroCusto;
        try {
            centroCusto = CentroCustoMapper.toEntity(dto, usuario);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        centroCusto = centroCustoRepo.save(centroCusto);
        return CentroCustoMapper.toDto(centroCusto);
    }


    @Transactional
    public CentroCustoDTO update(Long id, CentroCustoDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do centro de custo são obrigatórios");
        }

        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        // busca entidade existente
        CentroCusto existente = centroCustoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Centro de Custo não encontrado: id=" + id));

        Integer usuarioId = dto.getUsuarioId();
        Usuario usuario = usuarioRepo.findById(Long.valueOf(usuarioId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + usuarioId)
                );

        existente.setNome(dto.getNome());
        existente.setCodigo(dto.getCodigo());
        existente.setUsuario(usuario);


        CentroCusto atualizado = centroCustoRepo.save(existente);
        return CentroCustoMapper.toDto(atualizado);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        CentroCusto centroCusto = centroCustoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Centro de Custo não encontrado: id=" + id));


        centroCusto.setStatus(statusInativo());
        centroCustoRepo.save(centroCusto);
    }
}
