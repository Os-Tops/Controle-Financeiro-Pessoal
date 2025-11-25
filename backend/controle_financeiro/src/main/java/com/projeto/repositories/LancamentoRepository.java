package com.projeto.repositories;

import com.projeto.domains.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    Page<Lancamento> findByUsuario_Id(Integer usuarioId, Pageable pageable);
    Page<Lancamento> findByContaBancaria_Id(Long contaBancariaId, Pageable pageable);
    Page<Lancamento> findByCentroCusto_Id(Integer centroCustoId, Pageable pageable);
    Page<Lancamento> findByEntidade_Id(Integer entidadeId, Pageable pageable);
    Page<Lancamento> findByCartaoCredito_Id(Integer cartaoCreditoId, Pageable pageable);
}