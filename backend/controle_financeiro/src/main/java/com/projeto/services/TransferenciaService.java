package com.projeto.services;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.Transferencia;
import com.projeto.domains.dtos.TransferenciaDTO;
import com.projeto.mappers.TransferenciaMapper;
import com.projeto.repositories.ContaBancariaRepository;
import com.projeto.repositories.TransferenciaRepository;
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
public class TransferenciaService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final TransferenciaRepository transferenciaRepo;
    private final ContaBancariaRepository contaBancariaRepo;

    public TransferenciaService(TransferenciaRepository transferenciaRepo,
                                ContaBancariaRepository contaBancariaRepo) {
        this.transferenciaRepo = transferenciaRepo;
        this.contaBancariaRepo = contaBancariaRepo;
    }

    // ----------------------------------------------------------------------
    // LISTAGENS
    // ----------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<TransferenciaDTO> findAll() {
        return TransferenciaMapper.toDtoList(transferenciaRepo.findAll());
    }

    @Transactional(readOnly = true)
    public Page<TransferenciaDTO> findAll(Pageable pageable) {
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

        Page<Transferencia> page = transferenciaRepo.findAll(effective);
        return TransferenciaMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public Page<TransferenciaDTO> findAllByConta(Long contaId, Pageable pageable) {
        if (contaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId é obrigatório");
        }

        // valida existência da conta para erro claro
        if (!contaBancariaRepo.existsById(contaId)) {
            throw new ObjectNotFoundException("Conta bancária não encontrada: id=" + contaId);
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

        // conta como origem OU destino
        Page<Transferencia> page = transferenciaRepo
                .findByContaOrigem_IdOrContaDestino_Id(contaId, contaId, effective);

        return TransferenciaMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por conta (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> findAllByConta(Long contaId) {
        return findAllByConta(contaId, Pageable.unpaged()).getContent();
    }

    // ----------------------------------------------------------------------
    // FIND BY ID
    // ----------------------------------------------------------------------

    @Transactional(readOnly = true)
    public TransferenciaDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id da Transferência é obrigatório");
        }

        Transferencia transferencia = transferenciaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Transferência não encontrada: id=" + id)
                );

        return TransferenciaMapper.toDto(transferencia);
    }

    // ----------------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------------

    @Transactional
    public TransferenciaDTO create(TransferenciaDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da transferência são obrigatórios");
        }

        if (dto.getContaOrigemId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta de origem é obrigatório");
        }

        if (dto.getContaDestinoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta de destino é obrigatório");
        }

        ContaBancaria contaOrigem = contaBancariaRepo.findById(dto.getContaOrigemId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta de origem não encontrada: id=" + dto.getContaOrigemId())
                );

        ContaBancaria contaDestino = contaBancariaRepo.findById(dto.getContaDestinoId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta de destino não encontrada: id=" + dto.getContaDestinoId())
                );

        dto.setId(null);
        Transferencia transferencia;
        try {
            // assumindo mapper com assinatura: toEntity(dto, contaOrigem, contaDestino)
            transferencia = TransferenciaMapper.toEntity(dto, contaOrigem, contaDestino);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        transferencia = transferenciaRepo.save(transferencia);
        return TransferenciaMapper.toDto(transferencia);
    }

    // ----------------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------------

    @Transactional
    public TransferenciaDTO update(Long id, TransferenciaDTO dto) {

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id da Transferência é obrigatório");
        }

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da transferência são obrigatórios");
        }

        if (dto.getContaOrigemId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta de origem é obrigatório");
        }

        if (dto.getContaDestinoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta de destino é obrigatório");
        }

        // garante que a transferência existe (erro 404 se não existir)
        transferenciaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Transferência não encontrada: id=" + id)
                );

        ContaBancaria contaOrigem = contaBancariaRepo.findById(dto.getContaOrigemId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta de origem não encontrada: id=" + dto.getContaOrigemId())
                );

        ContaBancaria contaDestino = contaBancariaRepo.findById(dto.getContaDestinoId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta de destino não encontrada: id=" + dto.getContaDestinoId())
                );

        dto.setId(id);
        Transferencia transferencia;
        try {
            transferencia = TransferenciaMapper.toEntity(dto, contaOrigem, contaDestino);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        transferencia = transferenciaRepo.save(transferencia);
        return TransferenciaMapper.toDto(transferencia);
    }

    // ----------------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------------

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Transferencia transferencia = transferenciaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Transferência não encontrada: id=" + id)
                );

        transferenciaRepo.delete(transferencia);
    }
}
