package com.projeto.resources;

import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.services.LancamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lancamentos")
public class LancamentoResource {

    private final LancamentoService service;

    public LancamentoResource(LancamentoService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping
    public ResponseEntity<Page<LancamentoDTO>> list(
            @RequestParam(required = false) Integer usuarioId,
            @PageableDefault(size = 20, sort = "dataVencimento") Pageable pageable) {

        Page<LancamentoDTO> page = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping("/all")
    public ResponseEntity<List<LancamentoDTO>> listAll(
            @RequestParam(required = false) Integer usuarioId) {

        List<LancamentoDTO> body = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }
    
}
