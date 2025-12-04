package com.projeto.repositories;

import com.projeto.domains.MovimentoConta;
import com.projeto.domains.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;



@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>  {
    Page<Transferencia> findByContaOrigem_IdOrContaDestino_Id(
            Long contaOrigemId,
            Long contaDestinoId,
            Pageable pageable
    );
    List<Transferencia> findByDataBetweenAndContaOrigem_IdOrDataBetweenAndContaDestino_Id(
            LocalDate inicio1, LocalDate fim1, Long contaOrigemId,
            LocalDate inicio2, LocalDate fim2, Long contaDestinoId
    );
}
