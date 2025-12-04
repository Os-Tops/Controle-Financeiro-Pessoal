package com.projeto.repositories;

import com.projeto.domains.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>  {

    Page<Pagamento> findByLancamento_Id(Long lancamentoId, Pageable pageable);
    Page<Pagamento> findByContaBancaria_Id(Long contaBancariaId, Pageable pageable);

}
