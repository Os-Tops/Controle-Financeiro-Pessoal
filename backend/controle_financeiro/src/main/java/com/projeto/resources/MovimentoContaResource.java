package com.projeto.resources;

import com.projeto.domains.MovimentoConta;
import com.projeto.domains.dtos.MovimentoContaDTO;
import com.projeto.services.MovimentoContaService;
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
@RequestMapping("/api/v1/movimentoconta")
public class MovimentoContaResource {

    private final MovimentoContaService service;

    public MovimentoContaResource(MovimentoContaService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?grupoId=)
    @GetMapping
    public ResponseEntity<Page<MovimentoContaDTO>> list(
            @RequestParam(required = false) Integer contaId,
            @PageableDefault(size = 20, sort = "historico") Pageable pageable) {

        Page<MovimentoContaDTO> page = (contaId != null)
                ? service.findAllByConta(contaId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?grupoId=)
    @GetMapping("/all")
    public ResponseEntity<List<MovimentoContaDTO>> listAll(
            @RequestParam(required = false) Integer contaId) {

        List<MovimentoContaDTO> body = (contaId != null)
                ? service.findAllByConta(contaId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<MovimentoContaDTO>> findAllByConta(
            @PathVariable Long contaId,
            Pageable pageable
    ) {
        Page<MovimentoContaDTO> page = service.findAllByConta(contaId.intValue(), pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<MovimentoContaDTO> create(
            @RequestBody @Validated(MovimentoContaDTO.Create.class) MovimentoContaDTO dto) {

        MovimentoContaDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MovimentoContaDTO> update(@PathVariable Long id,
                                             @RequestBody @Validated(MovimentoContaDTO.Update.class) MovimentoContaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

