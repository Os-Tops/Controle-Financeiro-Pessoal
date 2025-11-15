package com.projeto.resources;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.services.ContaBancariaService;
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
@RequestMapping("/api/contabancaria")

public class ContaBancariaResource {

    private final ContaBancariaService service;

    public ContaBancariaResource(ContaBancariaService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<Page<ContaBancariaDTO>> list(
            @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 20, sort = "instituicao") Pageable pageable) {

        Page<ContaBancariaDTO> page = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaDTO> findById(@PathVariable Long id) {
        ContaBancariaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContaBancariaDTO> create(
            @RequestBody @Validated(ContaBancariaDTO.Create.class) ContaBancariaDTO dto) {

        ContaBancariaDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaDTO> update(@PathVariable Long id,
                                                    @RequestBody @Validated(ContaBancariaDTO.Update.class) ContaBancariaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
