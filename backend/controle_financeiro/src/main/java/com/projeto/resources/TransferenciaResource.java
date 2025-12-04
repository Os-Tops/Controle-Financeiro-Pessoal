package com.projeto.resources;

import com.projeto.domains.dtos.TransferenciaDTO;
import com.projeto.services.TransferenciaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/transferencia")
public class TransferenciaResource {

    private final TransferenciaService service;

    public TransferenciaResource(TransferenciaService service) {
        this.service = service;
    }

    /**
     * GET /api/v1/transferencias?inicio=YYYY-MM-DD&fim=YYYY-MM-DD&contaId=ID
     * Lista transferências por período e conta (origem ou destino).
     */
    @GetMapping
    public ResponseEntity<List<TransferenciaDTO>> listByPeriodoAndConta(
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam("contaId") Long contaId
    ) {
        List<TransferenciaDTO> body = service.findAllByContaAndPeriodo(inicio, fim, contaId);
        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/v1/transferencias/all
     * Lista todas as transferências (não paginado).
     * Opcionalmente filtra por conta (?contaId=).
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransferenciaDTO>> listAll(
            @RequestParam(required = false) Long contaId
    ) {
        List<TransferenciaDTO> body = (contaId != null)
                ? service.findAllByConta(contaId)
                : service.findAll();

        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/v1/transferencias/page
     * Lista transferências paginadas.
     * Opcionalmente filtra por conta (?contaId=).
     */
    @GetMapping("/page")
    public ResponseEntity<Page<TransferenciaDTO>> listPage(
            @RequestParam(required = false) Long contaId,
            @PageableDefault(size = 20, sort = "data") Pageable pageable
    ) {
        Page<TransferenciaDTO> page = (contaId != null)
                ? service.findAllByConta(contaId, pageable)
                : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * GET /api/v1/transferencias/{id}
     * Busca uma transferência específica.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> findById(@PathVariable Long id) {
        TransferenciaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * POST /api/v1/transferencias
     * Registra uma nova transferência.
     */
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

    /**
     * PUT /api/v1/transferencias/{id}
     * Atualiza uma transferência existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> update(@PathVariable Long id,
                                                   @RequestBody @Validated(TransferenciaDTO.Update.class) TransferenciaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * DELETE /api/v1/transferencias/{id}
     * Exclui uma transferência (se a regra de negócio permitir).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
