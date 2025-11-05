package com.projeto.repositories;

import com.projeto.domains.MovimentoConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovimentoContaRepository extends JpaRepository<MovimentoConta, Long> {
}
