package com.projeto.repositories;

import com.projeto.domains.CentroCusto;
import com.projeto.domains.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {

    Page<CentroCusto> findByUsuario_Id(Integer usuarioId, Pageable pageable);

    // agora com o enum Status
    Page<CentroCusto> findByStatus(Status status, Pageable pageable);

    List<CentroCusto> findByStatus(Status status);
}
