package com.projeto.repositories;

import com.projeto.domains.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>  {
}
