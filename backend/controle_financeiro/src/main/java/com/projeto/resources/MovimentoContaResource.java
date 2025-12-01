package com.projeto.resources;

import com.projeto.domains.dtos.MovimentoContaDTO;
import com.projeto.services.MovimentoContaService;
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
@RequestMapping("/api/v1")
public class MovimentoContaResource {

    private final MovimentoContaService service;

    public MovimentoContaResource(MovimentoContaService service) {
        this.service = service;
    }

    @GetMapping("/movimentoconta")
    public ResponseEntity<Page<MovimentoContaDTO>> list(
            @RequestParam(required = false) Integer contaId,
            @PageableDefault(size = 20, sort = "historico") Pageable pageable) {

        Page<MovimentoContaDTO> page = (contaId != null)
                ? service.findAllByConta(contaId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro

        return ResponseEntity.ok(page);
    }


    @GetMapping("/movimentoconta/all")
    public ResponseEntity<List<MovimentoContaDTO>> listAll(
            @RequestParam(required = false) Integer contaId) {

        List<MovimentoContaDTO> body = (contaId != null)
                ? service.findAllByConta(contaId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }


    @GetMapping("/movimentoconta/{contaId}")
    public ResponseEntity<Page<MovimentoContaDTO>> findAllByConta(
            @PathVariable Long contaId,
            Pageable pageable
    ) {
        Page<MovimentoContaDTO> page = service.findAllByConta(contaId.intValue(), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/movimentoconta/id/{id}")
    public ResponseEntity<MovimentoContaDTO> findById(@PathVariable Long id) {
        MovimentoContaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/movimentoconta")
    public ResponseEntity<MovimentoContaDTO> create(
            @RequestBody @Validated(MovimentoContaDTO.Create.class) MovimentoContaDTO dto) {

        MovimentoContaDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/movimentoconta/{id}")
    public ResponseEntity<MovimentoContaDTO> update(@PathVariable Long id,
                                                    @RequestBody @Validated(MovimentoContaDTO.Update.class) MovimentoContaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }


    @DeleteMapping("/movimentoconta/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorios/extrato")
    public ResponseEntity<List<MovimentoContaDTO>> gerarExtrato(
            @RequestParam("contaId") Integer contaId,
            @RequestParam("inicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        List<MovimentoContaDTO> movimentos = service.gerarExtrato(contaId, inicio, fim);
        return ResponseEntity.ok(movimentos);
    }

    @GetMapping("/relatorios/contas-a-pagar")
    public ResponseEntity<List<MovimentoContaDTO>> relatorioContasAPagar() {
        List<MovimentoContaDTO> movimentos = service.listarContasAPagar();
        return ResponseEntity.ok(movimentos);
    }

    @GetMapping("/relatorios/contas-a-receber")
    public ResponseEntity<List<MovimentoContaDTO>> relatorioContasAReceber() {
        List<MovimentoContaDTO> movimentos = service.listarContasAReceber();
        return ResponseEntity.ok(movimentos);
    }


    @GetMapping("/relatorios/posicao-geral")
    public ResponseEntity<Object> relatorioPosicaoGeral() {
        Object resumo = service.posicaoGeral();
        return ResponseEntity.ok(resumo);
    }
}
