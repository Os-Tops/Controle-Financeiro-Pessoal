package com.projeto.repositories;

import com.projeto.domains.FaturaCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FaturaCartaoRepository extends JpaRepository<FaturaCartao, Long> {
}
