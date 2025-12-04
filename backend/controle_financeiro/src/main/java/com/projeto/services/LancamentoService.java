package com.projeto.services;

import com.projeto.domains.*;
import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import com.projeto.mappers.LancamentoMapper;
import com.projeto.repositories.*;
import com.projeto.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.services.specs.LancamentoSpecs;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Service
public class LancamentoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final LancamentoRepository lancamentoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ContaBancariaRepository contaBancariaRepo;
    private final CentroCustoRepository  centroCustoRepo;
    private final EntidadeRepository entidadeRepo;
    private final CartaoCreditoRepository cartaoCreditoRepo;

    public LancamentoService(LancamentoRepository lancamentoRepo,
                             UsuarioRepository usuarioRepo,
                             ContaBancariaRepository contaBancariaRepo,
                             CentroCustoRepository centroCustoRepo,
                             EntidadeRepository entidadeRepo,
                             CartaoCreditoRepository cartaoCreditoRepo) {
        this.lancamentoRepo = lancamentoRepo;
        this.usuarioRepo = usuarioRepo;
        this.contaBancariaRepo = contaBancariaRepo;
        this.centroCustoRepo = centroCustoRepo;
        this.entidadeRepo = entidadeRepo;
        this.cartaoCreditoRepo = cartaoCreditoRepo;
    }

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findAll() {
        return LancamentoMapper.toDtoList(lancamentoRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findAll(Pageable pageable) {
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

        Page<Lancamento> page = lancamentoRepo.findAll(effective);
        return LancamentoMapper.toDtoPage(page);
    }

    /**
     * Busca paginada com múltiplos filtros dinâmicos
     */
    @Transactional(readOnly = true)
    public Page<LancamentoDTO> findWithFilters(TipoLancamento tipo, StatusLancamento status, LocalDate inicio,
                                               LocalDate fim, Pageable pageable) {

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

        Specification<Lancamento> spec = LancamentoSpecs.comFiltros(tipo, status, inicio, fim);

        Page<Lancamento> page = lancamentoRepo.findAll(spec, effective);

        return LancamentoMapper.toDtoPage(page);
    }

    // Sobrecarga para lista não paginada (Endpoint /all)
    @Transactional(readOnly = true)
    public List<LancamentoDTO> findListWithFilters(TipoLancamento tipo,
                                                   StatusLancamento status,
                                                   LocalDate inicio,
                                                   LocalDate fim) {
        // Reutiliza a lógica acima com unpaged
        return findWithFilters(tipo, status, inicio, fim, Pageable.unpaged()).getContent();
    }


    @Transactional(readOnly = true)
    public LancamentoDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return lancamentoRepo.findById(Long.valueOf(id))
                .map(LancamentoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Lancamento não encontrado: id=" + id));
    }

    //Create
    @Transactional
    public LancamentoDTO create(LancamentoDTO lancamentoDTO) {


        if (lancamentoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do lancamento são obrigatórios");
        }

        if (lancamentoDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Usuário é obrigatório");
        }

        if (lancamentoDTO.getContaBancariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Conta Bancária é obrigatório");
        }

        if (lancamentoDTO.getCentroCustoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Centro de Custo é obrigatório");
        }

        if (lancamentoDTO.getEntidadeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Entidade é obrigatório");
        }

        if (lancamentoDTO.getCartaoCreditoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Cartão de Crédito é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(Long.valueOf(lancamentoDTO.getUsuarioId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + lancamentoDTO.getUsuarioId())
                );

        ContaBancaria contaBancaria = contaBancariaRepo.findById(Long.valueOf(lancamentoDTO.getContaBancariaId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + lancamentoDTO.getContaBancariaId())
                );

        CentroCusto centroCusto = centroCustoRepo.findById(Long.valueOf(lancamentoDTO.getCentroCustoId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Centro de Custo não encontrado: id=" + lancamentoDTO.getCentroCustoId())
                );

        Entidade entidade = entidadeRepo.findById(Long.valueOf(lancamentoDTO.getEntidadeId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Entidade não encontrada: id=" + lancamentoDTO.getEntidadeId())
                );

        CartaoCredito cartaoCredito = cartaoCreditoRepo.findById(Long.valueOf(lancamentoDTO.getCartaoCreditoId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Cartão de Crédito não encontrado: id=" + lancamentoDTO.getCartaoCreditoId())
                );

        lancamentoDTO.setId(null);
        Lancamento lancamento;
        try{
            lancamento = LancamentoMapper.toEntity(lancamentoDTO, usuario, contaBancaria, centroCusto, entidade, cartaoCredito);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return LancamentoMapper.toDto(lancamentoRepo.save(lancamento));
    }

    //Update
    @Transactional
    public LancamentoDTO update(Long id, LancamentoDTO lancamentoDTO) {

        if (lancamentoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do lancamento são obrigatórios");
        }

        if (lancamentoDTO.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Usuário é obrigatório");
        }

        if (lancamentoDTO.getContaBancariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Conta Bancária é obrigatório");
        }

        if (lancamentoDTO.getCentroCustoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Centro de Custo é obrigatório");
        }

        if (lancamentoDTO.getEntidadeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Entidade é obrigatório");
        }

        if (lancamentoDTO.getCartaoCreditoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Cartão de Crédito é obrigatório");
        }

        Usuario usuario = usuarioRepo.findById(Long.valueOf(lancamentoDTO.getUsuarioId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + lancamentoDTO.getUsuarioId())
                );

        ContaBancaria contaBancaria = contaBancariaRepo.findById(Long.valueOf(lancamentoDTO.getContaBancariaId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Conta bancária não encontrada: id=" + lancamentoDTO.getContaBancariaId())
                );

        CentroCusto centroCusto = centroCustoRepo.findById(Long.valueOf(lancamentoDTO.getCentroCustoId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Centro de Custo não encontrado: id=" + lancamentoDTO.getCentroCustoId())
                );

        Entidade entidade = entidadeRepo.findById(Long.valueOf(lancamentoDTO.getEntidadeId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Entidade não encontrada: id=" + lancamentoDTO.getEntidadeId())
                );

        CartaoCredito cartaoCredito = cartaoCreditoRepo.findById(Long.valueOf(lancamentoDTO.getCartaoCreditoId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Cartão de Crédito não encontrado: id=" + lancamentoDTO.getCartaoCreditoId())
                );

        Lancamento lancamento = lancamentoRepo.findById(Long.valueOf(lancamentoDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Lancamento não encontrado: id=" + id));

        lancamentoDTO.setId(id);
        try{
            lancamento = LancamentoMapper.toEntity(lancamentoDTO,usuario, contaBancaria, centroCusto, entidade, cartaoCredito);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return LancamentoMapper.toDto(lancamentoRepo.save(lancamento));
    }
    
    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Lancamento lancamento = lancamentoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Lancamento não encontrado: id=" + id));

        lancamentoRepo.delete(lancamento);
    }
    
}
