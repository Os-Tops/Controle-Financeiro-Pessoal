package com.projeto.repositories;

import com.projeto.domains.CentroCusto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {
    Page<CentroCusto> findByUsuario_Id(Integer usuarioId, Pageable pageable);
}
