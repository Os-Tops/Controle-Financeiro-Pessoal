package com.projeto.services;

import com.projeto.domains.TelefoneUsuario;
import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.TelefoneUsuarioDTO;
import com.projeto.mappers.TelefoneUsuarioMapper;
import com.projeto.repositories.TelefoneUsuarioRepository;
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
public class TelefoneUsuarioService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final TelefoneUsuarioRepository telefoneUsuarioRepo;
    private final UsuarioRepository usuarioRepo;

    public TelefoneUsuarioService(TelefoneUsuarioRepository telefoneUsuarioRepo,
                           UsuarioRepository usuarioRepo) {
        this.telefoneUsuarioRepo = telefoneUsuarioRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /* =================== READ =================== */

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<TelefoneUsuarioDTO> findAll() {
        return TelefoneUsuarioMapper.toDtoList(telefoneUsuarioRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<TelefoneUsuarioDTO> findAll(Pageable pageable) {
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

        Page<TelefoneUsuario> page = telefoneUsuarioRepo.findAll(effective);
        return TelefoneUsuarioMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por grupo */
    @Transactional(readOnly = true)
    public Page<TelefoneUsuarioDTO> findAllByUsuario(Integer usuarioId, Pageable pageable) {
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

        Page<TelefoneUsuario> page = telefoneUsuarioRepo.findByUsuario_Id(usuarioId, effective);
        return TelefoneUsuarioMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por grupo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<TelefoneUsuarioDTO> findAllByUsuario(Integer usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }



    @Transactional(readOnly = true)
    public TelefoneUsuarioDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return telefoneUsuarioRepo.findById(Long.valueOf(id))
                .map(TelefoneUsuarioMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("TelefoneUsuario não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public TelefoneUsuarioDTO create(TelefoneUsuarioDTO entidadeDTO) {


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
        TelefoneUsuario entidade;
        try{
            entidade = TelefoneUsuarioMapper.toEntity(entidadeDTO, usuario);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return TelefoneUsuarioMapper.toDto(telefoneUsuarioRepo.save(entidade));
    }

    //Update
    @Transactional
    public TelefoneUsuarioDTO update(Long id, TelefoneUsuarioDTO entidadeDTO) {

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

        TelefoneUsuario entidade = telefoneUsuarioRepo.findById(Long.valueOf(entidadeDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("TelefoneUsuario não encontrado: id=" + id));

        entidadeDTO.setId(id);
        try{
            entidade = TelefoneUsuarioMapper.toEntity(entidadeDTO, usuario);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return TelefoneUsuarioMapper.toDto(telefoneUsuarioRepo.save(entidade));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        TelefoneUsuario entidade = telefoneUsuarioRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("TelefoneUsuario não encontrada: id=" + id));

        telefoneUsuarioRepo.delete(entidade);
    }

}
