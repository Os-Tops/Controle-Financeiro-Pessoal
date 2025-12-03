package com.projeto.resources;

import com.projeto.domains.dtos.EntidadeDTO;
import com.projeto.domains.dtos.EntidadeDTO;
import com.projeto.services.EntidadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entidades")
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

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeDTO> findById(@PathVariable Integer id) {
        EntidadeDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EntidadeDTO> create(
            @RequestBody @Validated(EntidadeDTO.Create.class) EntidadeDTO dto) {

        EntidadeDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadeDTO> update(@PathVariable Long id,
                                                @RequestBody @Validated(EntidadeDTO.Update.class) EntidadeDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
