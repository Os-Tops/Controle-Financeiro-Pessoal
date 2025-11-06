package com.projeto.services;

import com.projeto.domains.dtos.UsuarioDTO;
import com.projeto.mappers.UsuarioMapper;
import com.projeto.repositories.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    // Injeção por construtor (Spring injeta automaticamente se houver só um construtor público)
    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll(){
        //retorna uma lista de ProdutoDTO
        return UsuarioMapper.toDtoList(usuarioRepo.findAll());
    }

}
