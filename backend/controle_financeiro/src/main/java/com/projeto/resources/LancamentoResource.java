package com.projeto.resources;

import com.projeto.domains.dtos.LancamentoDTO;
import com.projeto.domains.dtos.PagamentoDTO;
import com.projeto.domains.dtos.RecebimentoDTO;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import com.projeto.services.LancamentoService;
import com.projeto.services.PagamentoService;
import com.projeto.services.RecebimentoService;
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
@RequestMapping("/api/v1/lancamentos")
public class LancamentoResource {

    private final LancamentoService service;
    private final PagamentoService pagamentoService;
    private final RecebimentoService recebimentoService;

    public LancamentoResource(LancamentoService service,  PagamentoService pagamentoService, RecebimentoService recebimentoService) {
        this.service = service;
        this.pagamentoService = pagamentoService;
        this.recebimentoService = recebimentoService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<LancamentoDTO>> list(
            @RequestParam(required = false) TipoLancamento tipo,
            @RequestParam(required = false) StatusLancamento status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @PageableDefault(size = 20, sort = "dataVencimento") Pageable pageable) {

        Page<LancamentoDTO> page = service.findWithFilters(tipo, status, inicio, fim, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping
    public ResponseEntity<List<LancamentoDTO>> listAll(
            @RequestParam(required = false) TipoLancamento tipo,
            @RequestParam(required = false) StatusLancamento status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<LancamentoDTO> body = service.findListWithFilters(tipo, status, inicio, fim);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoDTO> findById(@PathVariable Integer id) {
        LancamentoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LancamentoDTO> create(
            @RequestBody @Validated(LancamentoDTO.Create.class) LancamentoDTO dto) {

        LancamentoDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoDTO> update(@PathVariable Long id,
                                             @RequestBody @Validated(LancamentoDTO.Update.class) LancamentoDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*-----------------------------------------------PAGAMENTO E RECEBIMENTO-----------------------------------------------*/

    @GetMapping("/{id}/pagamentos")
    public ResponseEntity<Page<PagamentoDTO>> listPagamentosDoLancamento(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "dataPagamento") Pageable pageable) {

        Page<PagamentoDTO> page = pagamentoService.findAllByLancamento(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{id}/pagamentos")
    public ResponseEntity<PagamentoDTO> create(
            @PathVariable Integer id,
            @RequestBody @Validated(PagamentoDTO.Create.class) PagamentoDTO dto) {

        PagamentoDTO created = pagamentoService.create(dto, id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}/recebimentos")
    public ResponseEntity<Page<RecebimentoDTO>> listRecebimentosDoLancamento(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "dataRecebimento") Pageable pageable) {

        Page<RecebimentoDTO> page = recebimentoService.findAllByLancamento(id, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{id}/recebimentos")
    public ResponseEntity<RecebimentoDTO> create(
            @PathVariable Integer id,
            @RequestBody @Validated(RecebimentoDTO.Create.class) RecebimentoDTO dto) {

        RecebimentoDTO created = recebimentoService.create(dto, id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
    
}
