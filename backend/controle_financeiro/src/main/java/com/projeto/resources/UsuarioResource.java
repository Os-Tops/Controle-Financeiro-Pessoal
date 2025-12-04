package com.projeto.resources;

import com.projeto.domains.dtos.UsuarioDTO;
import com.projeto.services.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioResource {
    private final UsuarioService service;

    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }

    // GET não paginado (simples e direto)
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioDTO>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // GET "paginado" embrulhado (usa findAll e monta PageImpl)
    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> list(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        List<UsuarioDTO> all = service.findAll();
        Page<UsuarioDTO> page = new PageImpl<>(all, pageable, all.size());
        return ResponseEntity.ok(page);
    }
}
