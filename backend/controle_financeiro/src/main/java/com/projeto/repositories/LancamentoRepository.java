package com.projeto.repositories;

import com.projeto.domains.Lancamento;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    Page<Lancamento> findByUsuario_Id(Integer usuarioId, Pageable pageable);
    Page<Lancamento> findByContaBancaria_Id(Long contaBancariaId, Pageable pageable);
    Page<Lancamento> findByStatusLancamento(StatusLancamento status, Pageable pageable);
    Page<Lancamento> findByTipoLancamento(TipoLancamento tipo, Pageable pageable);
    Page<Lancamento> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);
}