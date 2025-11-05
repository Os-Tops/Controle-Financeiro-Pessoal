package com.projeto.repositories;

import com.projeto.domains.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
}
