package com.projeto.resources;

import com.projeto.domains.ContaBancaria;
import com.projeto.domains.dtos.ContaBancariaDTO;
import com.projeto.services.ContaBancariaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDate;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/contas")
public class ContaBancariaResource {

    private final ContaBancariaService service;

    public ContaBancariaResource(ContaBancariaService service) {
        this.service = service;
    }

    // GET paginado; filtro por usuário opcional (?usuarioId=)
    @GetMapping
    public ResponseEntity<Page<ContaBancariaDTO>> list(
            @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 20, sort = "instituicao") Pageable pageable) {

        Page<ContaBancariaDTO> page = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId, pageable) // paginado + filtro
                : service.findAll(pageable);                    // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por usuário opcional (?usuarioId=)
    @GetMapping("/all")
    public ResponseEntity<List<ContaBancariaDTO>> listAll(
            @RequestParam(required = false) Long usuarioId) {

        List<ContaBancariaDTO> body = (usuarioId != null)
                ? service.findAllByUsuario(usuarioId) // não paginado + filtro
                : service.findAll();                  // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

    // GET paginado por usuário via path /usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<ContaBancariaDTO>> findAllByUsuario(
            @PathVariable Long usuarioId,
            Pageable pageable
    ) {
        Page<ContaBancariaDTO> page = service.findAllByUsuario(usuarioId, pageable);
        return ResponseEntity.ok(page);
    }

    // GET por id (uma conta específica)
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaDTO> findById(@PathVariable Long id) {
        ContaBancariaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContaBancariaDTO> create(
            @RequestBody @Validated(ContaBancariaDTO.Create.class) ContaBancariaDTO dto
    ) {
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
    @GetMapping("/{id}/extrato")
    public ResponseEntity<ContaBancariaDTO> obterExtrato(
            @PathVariable Long id,
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(name = "modo", defaultValue = "contabilprojetado") String modo
    ) {
        ContaBancariaDTO extrato = service.gerarExtrato(id, inicio, fim, modo);
        return ResponseEntity.ok(extrato);
    }
}
