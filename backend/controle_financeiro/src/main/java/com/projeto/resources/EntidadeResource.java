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

    @GetMapping("/all")
    public ResponseEntity<Page<EntidadeDTO>> list(
            @RequestParam(required = false) String nome, //
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<EntidadeDTO> page = service.findByNome(nome, pageable);
        return ResponseEntity.ok(page);
    }

    // GET Lista completa (sem paginação)
    @GetMapping
    public ResponseEntity<List<EntidadeDTO>> listAll(
            @RequestParam(required = false) String nome) {

        List<EntidadeDTO> body = service.findByNome(nome);
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
