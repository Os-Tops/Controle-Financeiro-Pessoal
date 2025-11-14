package com.projeto.repositories;

import com.projeto.domains.MovimentoConta;
import com.projeto.domains.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>  {
    Page<Transferencia> findByConta_Id(Integer contaId, Pageable pageable);
}
