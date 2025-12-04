package com.projeto.repositories;

import com.projeto.domains.MovimentoConta;
import com.projeto.domains.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimentoContaRepository extends JpaRepository<MovimentoConta, Long> {

    Page<MovimentoConta> findByContaBancaria_Id(Long contaId, Pageable pageable);

    List<MovimentoConta> findByContaBancaria_IdAndDataMovimentoBetween(
            Long contaId, LocalDate inicio, LocalDate fim);

    // usa o nome do campo da entidade: tipoTransacao
    List<MovimentoConta> findByTipoTransacao(TipoTransacao tipoTransacao);

    @Query("SELECT SUM(m.valor) FROM MovimentoConta m WHERE m.tipoTransacao = :tipo")
    Double sumByTipo(@Param("tipo") TipoTransacao tipoTransacao);
}
