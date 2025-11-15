package com.projeto.services;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.MovimentoConta;
import com.projeto.domains.dtos.MovimentoContaDTO;
import com.projeto.mappers.MovimentoContaMapper;
import com.projeto.repositories.ContaBancariaRepository;
import com.projeto.repositories.MovimentoContaRepository;
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
public class MovimentoContaService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final MovimentoContaRepository movimentoContaRepo;
    private final ContaBancariaRepository contaBancariaRepo;

    public MovimentoContaService(MovimentoContaRepository movimentoContaRepo,
                                 ContaBancariaRepository contaBancariaRepo) {
        this.movimentoContaRepo = movimentoContaRepo;
        this.contaBancariaRepo = contaBancariaRepo;
    }

    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> findAll() {
        return MovimentoContaMapper.toDtoList(movimentoContaRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<MovimentoContaDTO> findAll(Pageable pageable) {
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

        Page<MovimentoConta> page = movimentoContaRepo.findAll(effective);
        return MovimentoContaMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<MovimentoContaDTO> findAllByConta(Integer contaId, Pageable pageable) {
        if (contaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId é obrigatório");
        }

        // valida existência do grupo para erro claro
        if (!contaBancariaRepo.existsById(Long.valueOf(contaId))) {
            throw new ObjectNotFoundException("Conta bancária não encontrado: id=" + contaId);
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

        Page<MovimentoConta> page =
                movimentoContaRepo.findByContaBancaria_Id(contaId.longValue(), effective);
        return MovimentoContaMapper.toDtoPage(page);
    }
    /** Não paginado, filtrando por grupo (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> findAllByConta(Integer contaId) {
        return findAllByConta(contaId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public MovimentoContaDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id do Movimento Conta é obrigatório");
        }

        return movimentoContaRepo.findById(id)
                .map(MovimentoContaMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Movimento Conta não encontrado: id=" + id));
    }


    @Transactional
    public MovimentoContaDTO create(MovimentoContaDTO movimentoContaDTO) {


        if (movimentoContaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do produto são obrigatórios");
        }

        if (movimentoContaDTO.getContaBancariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta bancária é obrigatório");
        }

        ContaBancaria contaBancaria = contaBancariaRepo.findById(movimentoContaDTO.getContaBancariaId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Grupo de Produto não encontrado: id=" + movimentoContaDTO.getContaBancariaId())
                );

        movimentoContaDTO.setId(null);
        MovimentoConta movimentoConta;
        try {
            movimentoConta = MovimentoContaMapper.toEntity(movimentoContaDTO, contaBancaria);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return MovimentoContaMapper.toDto(movimentoContaRepo.save(movimentoConta));
    }

    @Transactional
    public MovimentoContaDTO update(Long id, MovimentoContaDTO movimentoContaDTO) {


        if (movimentoContaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do produto são obrigatórios");
        }

        if (movimentoContaDTO.getContaBancariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do grupo de produto é obrigatório");
        }

        ContaBancaria contaBancaria = contaBancariaRepo.findById(movimentoContaDTO.getContaBancariaId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Grupo de Produto não encontrado: id=" + movimentoContaDTO.getContaBancariaId())
                );

        MovimentoConta movimentoConta = movimentoContaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Produto não encontrado: id=" + id));

        movimentoContaDTO.setId(id);
        try {
            movimentoConta = MovimentoContaMapper.toEntity(movimentoContaDTO, contaBancaria);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return MovimentoContaMapper.toDto(movimentoContaRepo.save(movimentoConta));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        MovimentoConta movimentoConta = movimentoContaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Grupo de Produto não encontrado: id=" + id));

        movimentoContaRepo.delete(movimentoConta);
    }

}
