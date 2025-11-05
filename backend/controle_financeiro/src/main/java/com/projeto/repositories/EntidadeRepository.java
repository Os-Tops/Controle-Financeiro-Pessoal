package com.projeto.repositories;

import com.projeto.domains.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EntidadeRepository extends JpaRepository<Entidade, Long> {
}
