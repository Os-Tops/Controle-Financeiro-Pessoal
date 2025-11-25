package com.projeto.repositories;

import com.projeto.domains.Recebimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface RecebimentoRepository extends JpaRepository<Recebimento, Long>  {

    Page<Recebimento> findByLancamento_Id(Long lancamentoId, Pageable pageable);
    Page<Recebimento> findByContaBancaria_Id(Long contaBancariaId, Pageable pageable);

}
