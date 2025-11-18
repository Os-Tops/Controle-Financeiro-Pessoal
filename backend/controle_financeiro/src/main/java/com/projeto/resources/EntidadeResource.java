package com.projeto.resources;

import com.projeto.domains.dtos.EntidadeDTO;
import com.projeto.services.EntidadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entidade")
public class EntidadeResource {

    private final EntidadeService service;

    public EntidadeResource(EntidadeService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping
    public ResponseEntity<Page<EntidadeDTO>> list(
            @RequestParam(required = false) Integer usuarioId,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<EntidadeDTO> page = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping("/all")
    public ResponseEntity<List<EntidadeDTO>> listAll(
            @RequestParam(required = false) Integer usuarioId) {

        List<EntidadeDTO> body = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }
    
}
