package com.projeto.services;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.Usuario;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.mappers.ContaBancariaMapper;
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
    public Page<ContaBancariaDTO> findAllByUsuario(Long usuarioId, Pageable pageable) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId é obrigatório");
        }

        // valida existência do usuário para erro claro
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new ObjectNotFoundException("Usuário não encontrado: id=" + usuarioId);
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
    public List<ContaBancariaDTO> findAllByUsuario(Long usuarioId) {
        return findAllByUsuario(usuarioId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public ContaBancariaDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id da Conta Bancária é obrigatório");
        }

        return contaBancariaRepo.findById(id)
                .map(ContaBancariaMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta Bancária não encontrada: id=" + id));
    }

    @Transactional
    public ContaBancariaDTO create(ContaBancariaDTO contaBancariaDTO) {

        if (contaBancariaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da conta bancária são obrigatórios");
        }

        if (contaBancariaDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(contaBancariaDTO.getUsuarioId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + contaBancariaDTO.getUsuarioId())
                );

        contaBancariaDTO.setId(null);
        ContaBancaria contaBancaria;
        try {
            contaBancaria = ContaBancariaMapper.toEntity(contaBancariaDTO, usuario);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return ContaBancariaMapper.toDto(contaBancariaRepo.save(contaBancaria));
    }

    @Transactional
    public ContaBancariaDTO update(Long id, ContaBancariaDTO contaBancariaDTO) {

        if (contaBancariaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da conta bancária são obrigatórios");
        }

        if (contaBancariaDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(contaBancariaDTO.getUsuarioId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + contaBancariaDTO.getUsuarioId())
                );

        ContaBancaria contaBancaria = contaBancariaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + id));

        contaBancariaDTO.setId(id);
        try {
            contaBancaria = ContaBancariaMapper.toEntity(contaBancariaDTO, usuario);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return ContaBancariaMapper.toDto(contaBancariaRepo.save(contaBancaria));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        ContaBancaria contaBancaria = contaBancariaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + id));

        contaBancariaRepo.delete(contaBancaria);
    }


    @Transactional(readOnly = true)
    public ContaBancariaDTO gerarExtrato(Long contaId,
                                         LocalDate inicio,
                                         LocalDate fim,
                                         String modo) {

        if (contaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id da Conta Bancária é obrigatório");
        }
        if (inicio == null || fim == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros 'inicio' e 'fim' são obrigatórios");
        }
        if (fim.isBefore(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data fim não pode ser anterior à data início");
        }

        ContaBancaria conta = contaBancariaRepo.findById(contaId)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta Bancária não encontrada: id=" + contaId)
                );

        // Por enquanto, só devolve os dados da conta.
        // Depois você pode incluir saldos, lançamentos etc. numa DTO específica, se quiser.
        return ContaBancariaMapper.toDto(conta);
    }
}