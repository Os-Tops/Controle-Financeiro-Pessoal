package com.projeto.repositories;

import com.projeto.domains.CartaoCredito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    Page<CartaoCredito> findByUsuario_Id(Integer usuarioId, Pageable pageable);
}
