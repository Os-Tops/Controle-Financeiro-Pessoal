package com.projeto.resources;

import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.services.CartaoCreditoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CartaoCreditoResource {

    private final CartaoCreditoService service;

    public CartaoCreditoResource(CartaoCreditoService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping
    public ResponseEntity<Page<CartaoCreditoDTO>> list(
            @RequestParam(required = false) Integer usuarioId,
            @PageableDefault(size = 20, sort = "apelido") Pageable pageable) {

        Page<CartaoCreditoDTO> page = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?usuarioId=)
    @GetMapping("/all")
    public ResponseEntity<List<CartaoCreditoDTO>> listAll(
            @RequestParam(required = false) Integer usuarioId) {

        List<CartaoCreditoDTO> body = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }
    
}
