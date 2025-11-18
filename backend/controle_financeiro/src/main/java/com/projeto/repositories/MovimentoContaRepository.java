package com.projeto.repositories;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.MovimentoConta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovimentoContaRepository extends JpaRepository<MovimentoConta, Long> {
    Page<MovimentoConta> findByContaBancaria_Id(Long contaId, Pageable pageable);
}
