package com.projeto.repositories;

import com.projeto.domains.CartaoCredito;
import com.projeto.domains.ContaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    Page<ContaBancaria> findByUsuario_Id(Long usuarioId, Pageable pageable);
}
