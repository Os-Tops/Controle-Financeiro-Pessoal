package com.projeto.repositories;

import com.projeto.domains.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
}
