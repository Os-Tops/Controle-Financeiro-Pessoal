package com.projeto.resources;

import com.projeto.domains.Transferencia;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.domains.dtos.TransferenciaDTO;
import com.projeto.services.ContaBancariaService;
import com.projeto.services.TransferenciaService;
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
@RequestMapping("/api/transferencia")
public class TransferenciaResource {

    private final TransferenciaService service;

    public TransferenciaResource(TransferenciaService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<Page<TransferenciaDTO>> list(
            @RequestParam(required = false) Long contaId,
            @PageableDefault(size = 20, sort = "data") Pageable pageable) {

        Page<TransferenciaDTO> page = (contaId != null)
                ? service.findAllByConta(contaId, pageable)   // filtro por conta (origem ou destino)
                : service.findAll(pageable);                  // sem filtro, só paginação

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> findById(@PathVariable Long id) {
        TransferenciaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TransferenciaDTO> create(
            @RequestBody @Validated(TransferenciaDTO.Create.class) TransferenciaDTO dto) {

        TransferenciaDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> update(@PathVariable Long id,
                                                   @RequestBody @Validated(TransferenciaDTO.Update.class) TransferenciaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
