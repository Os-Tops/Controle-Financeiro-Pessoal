package com.projeto.resources;

import com.projeto.domains.dtos.CentroCustoDTO;
import com.projeto.services.CentroCustoService;
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
@RequestMapping("/api/v1/centros-custo")
public class CentroCustoResource {

    private final CentroCustoService service;

    public CentroCustoResource(CentroCustoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CentroCustoDTO>> list(
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<CentroCustoDTO> page =
                (ativo != null)
                        ? service.findAllByAtivo(ativo, pageable)
                        : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/all")
    public ResponseEntity<List<CentroCustoDTO>> listAll(
            @RequestParam(required = false) Boolean ativo) {

        List<CentroCustoDTO> body =
                (ativo != null)
                        ? service.findAllByAtivo(ativo)
                        : service.findAll();

        return ResponseEntity.ok(body);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CentroCustoDTO> findById(@PathVariable Long id) {
        CentroCustoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<CentroCustoDTO> create(
            @RequestBody @Validated(CentroCustoDTO.Create.class) CentroCustoDTO dto
    ) {
        CentroCustoDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroCustoDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated(CentroCustoDTO.Update.class) CentroCustoDTO dto
    ) {
        dto.setId(id);
        CentroCustoDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
