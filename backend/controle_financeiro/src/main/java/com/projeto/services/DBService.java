package com.projeto.services;

import com.projeto.domains.Usuario;
import com.projeto.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DBService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public void initDB(){
        Usuario usuario01 = new Usuario(null,"João",
                "joaozinho@gameplay.com", LocalDate.of(2025, 10, 25));
        Usuario usuario02 = new Usuario(null,"Ana",
                "aninha@bol.com", LocalDate.of(2022, 8, 14));

        usuarioRepo.save(usuario01);
        usuarioRepo.save(usuario02);
    }

}
