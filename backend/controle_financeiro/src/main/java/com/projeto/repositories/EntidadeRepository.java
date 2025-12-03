package com.projeto.repositories;

import com.projeto.domains.Entidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EntidadeRepository extends JpaRepository<Entidade, Long> {
    Page<Entidade> findByUsuario_Id(Integer usuarioId, Pageable pageable);
    Page<Entidade> findByNome(String nome, Pageable pageable);
}
