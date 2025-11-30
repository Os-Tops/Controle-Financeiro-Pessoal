package com.projeto.services;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.MovimentoConta;
import com.projeto.domains.dtos.MovimentoContaDTO;
import com.projeto.domains.enums.TipoTransacao;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimentoContaService {

    private static final int MAX_PAGE_SIZE = 200;

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
        if (contaId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId é obrigatório");

        if (!contaBancariaRepo.existsById(contaId.longValue()))
            throw new ObjectNotFoundException("Conta bancária não encontrada: id=" + contaId);

        final Pageable effective =
                (pageable == null || pageable.isUnpaged())
                        ? Pageable.unpaged()
                        : PageRequest.of(
                        Math.max(0, pageable.getPageNumber()),
                        Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                        pageable.getSort()
                );

        Page<MovimentoConta> page =
                movimentoContaRepo.findByContaBancaria_Id(contaId.longValue(), effective);

        return MovimentoContaMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> findAllByConta(Integer contaId) {
        return findAllByConta(contaId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public MovimentoContaDTO findById(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");

        return movimentoContaRepo.findById(id)
                .map(MovimentoContaMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Movimento Conta não encontrado: id=" + id));
    }

    @Transactional
    public MovimentoContaDTO create(MovimentoContaDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        if (dto.getContaBancariaId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta é obrigatório");

        ContaBancaria conta = contaBancariaRepo.findById(dto.getContaBancariaId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + dto.getContaBancariaId()));

        dto.setId(null);
        MovimentoConta entidade = MovimentoContaMapper.toEntity(dto, conta);

        return MovimentoContaMapper.toDto(movimentoContaRepo.save(entidade));
    }

    @Transactional
    public MovimentoContaDTO update(Long id, MovimentoContaDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        if (dto.getContaBancariaId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da conta bancária é obrigatório");

        movimentoContaRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Movimento Conta não encontrado: id=" + id));

        ContaBancaria conta = contaBancariaRepo.findById(dto.getContaBancariaId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + dto.getContaBancariaId()));

        dto.setId(id);
        MovimentoConta entidade = MovimentoContaMapper.toEntity(dto, conta);

        return MovimentoContaMapper.toDto(movimentoContaRepo.save(entidade));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");

        MovimentoConta mov = movimentoContaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Movimento Conta não encontrado: id=" + id));

        movimentoContaRepo.delete(mov);
    }


    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> gerarExtrato(Integer contaId, LocalDate inicio, LocalDate fim) {

        if (contaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId é obrigatório");
        }

        if (!contaBancariaRepo.existsById(contaId.longValue())) {
            throw new ObjectNotFoundException("Conta bancária não encontrada: id=" + contaId);
        }

        List<MovimentoConta> lista =
                movimentoContaRepo.findByContaBancaria_IdAndDataMovimentoBetween(
                        contaId.longValue(), inicio, fim);

        return MovimentoContaMapper.toDtoList(lista);
    }

    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> listarContasAPagar() {
        List<MovimentoConta> lista =
                movimentoContaRepo.findByTipoTransacao(TipoTransacao.DEBITO);

        return MovimentoContaMapper.toDtoList(lista);
    }


    @Transactional(readOnly = true)
    public List<MovimentoContaDTO> listarContasAReceber() {
        List<MovimentoConta> lista =
                movimentoContaRepo.findByTipoTransacao(TipoTransacao.CREDITO);

        return MovimentoContaMapper.toDtoList(lista);
    }

    @Transactional(readOnly = true)
    public Object posicaoGeral() {

        Double totalPagar = movimentoContaRepo.sumByTipo(TipoTransacao.DEBITO);
        Double totalReceber = movimentoContaRepo.sumByTipo(TipoTransacao.CREDITO);

        double contasAPagar = totalPagar != null ? totalPagar : 0.0;
        double contasAReceber = totalReceber != null ? totalReceber : 0.0;
        double saldoGeral = contasAReceber - contasAPagar;

        // objeto simples que vira JSON
        return new Object() {
            public final double contas_a_pagar = contasAPagar;
            public final double contas_a_receber = contasAReceber;
            public final double saldo_geral = saldoGeral;
        };
    }
}