package com.projeto.services;

import com.projeto.domains.*;
import com.projeto.domains.dtos.EntidadeDTO;
import com.projeto.domains.dtos.EntidadeDTO;
import com.projeto.mappers.EntidadeMapper;
import com.projeto.mappers.EntidadeMapper;
import com.projeto.repositories.UsuarioRepository;
import com.projeto.repositories.EntidadeRepository;
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
public class EntidadeService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final EntidadeRepository entidadeRepo;
    private final UsuarioRepository usuarioRepo;

    public EntidadeService(EntidadeRepository entidadeRepo,
                          UsuarioRepository usuarioRepo) {
        this.entidadeRepo = entidadeRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /* =================== READ =================== */

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<EntidadeDTO> findAll() {
        return EntidadeMapper.toDtoList(entidadeRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<EntidadeDTO> findAll(Pageable pageable) {
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

        Page<Entidade> page = entidadeRepo.findAll(effective);
        return EntidadeMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por grupo */
    @Transactional(readOnly = true)
    public Page<EntidadeDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
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

        Page<Entidade> page = entidadeRepo.findByUsuario_Id(usuarioId, effective);
        return EntidadeMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por grupo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<EntidadeDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Page<EntidadeDTO> findByNome(String nome, Pageable pageable) {

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

        Page<Entidade> page=Page.empty();

        if (nome != null) {
            page = entidadeRepo.findByNome(nome, effective);
            if(page.isEmpty()){
                throw new ObjectNotFoundException("Nenhuma entidade encontrada com o nome: " + nome);
            }
        }

        return EntidadeMapper.toDtoPage(page);
    }

    /**
     * Busca lista completa (sem paginação) com filtros
     * Reutiliza a lógica acima passando Pageable.unpaged()
     */
    @Transactional(readOnly = true)
    public List<EntidadeDTO> findByNome(String nome) {
        return findByNome(nome, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public EntidadeDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return entidadeRepo.findById(Long.valueOf(id))
                .map(EntidadeMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Entidade não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public EntidadeDTO create(EntidadeDTO entidadeDTO) {


        if (entidadeDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da entidade são obrigatórios");
        }

        if (entidadeDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Usuário é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(Long.valueOf(entidadeDTO.getUsuarioId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + entidadeDTO.getUsuarioId())
                );

        entidadeDTO.setId(null);
        Entidade entidade;
        try{
            entidade = EntidadeMapper.toEntity(entidadeDTO, usuario);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return EntidadeMapper.toDto(entidadeRepo.save(entidade));
    }

    //Update
    @Transactional
    public EntidadeDTO update(Long id, EntidadeDTO entidadeDTO) {

        if (entidadeDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do entidade são obrigatórios");
        }

        if (entidadeDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Usuário é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(Long.valueOf(entidadeDTO.getUsuarioId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + entidadeDTO.getUsuarioId())
                );

        Entidade entidade = entidadeRepo.findById(Long.valueOf(entidadeDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Entidade não encontrado: id=" + id));

        entidadeDTO.setId(id);
        try{
            entidade = EntidadeMapper.toEntity(entidadeDTO, usuario);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return EntidadeMapper.toDto(entidadeRepo.save(entidade));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Entidade entidade = entidadeRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Entidade não encontrada: id=" + id));

        entidadeRepo.delete(entidade);
    }
    
}
