package com.projeto.resources;

import com.projeto.domains.dtos.CartaoCreditoDTO;
import com.projeto.services.CartaoCreditoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
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

    @GetMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> findById(@PathVariable Long id) {
        CartaoCreditoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CartaoCreditoDTO> create(
            @RequestBody @Validated(CartaoCreditoDTO.Create.class) CartaoCreditoDTO dto) {

        CartaoCreditoDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoCreditoDTO> update(@PathVariable Long id,
                                             @RequestBody @Validated(CartaoCreditoDTO.Update.class) CartaoCreditoDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
