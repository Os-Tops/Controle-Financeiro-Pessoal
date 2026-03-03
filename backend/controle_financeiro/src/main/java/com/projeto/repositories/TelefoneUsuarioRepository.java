package com.projeto.repositories;

import com.projeto.domains.TelefoneUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TelefoneUsuarioRepository extends JpaRepository<TelefoneUsuario, Long> {
    Page<TelefoneUsuario> findByUsuario_Id(Integer usuarioId, Pageable pageable);
}
