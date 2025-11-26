package com.projeto.resources;

import com.projeto.domains.dtos.RecebimentoDTO;
import com.projeto.services.RecebimentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recebimento")
public class RecebimentoResource {

    private final RecebimentoService service;

    public RecebimentoResource(RecebimentoService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?lancamentoId=)
    @GetMapping
    public ResponseEntity<Page<RecebimentoDTO>> list(
            @RequestParam(required = false) Integer lancamentoId,
            @PageableDefault(size = 20, sort = "dataRecebimento") Pageable pageable) {

        Page<RecebimentoDTO> page = (lancamentoId != null)
                ? service.findAllByLancamento(Long.valueOf(lancamentoId), pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?lancamentoId=)
    @GetMapping("/all")
    public ResponseEntity<List<RecebimentoDTO>> listAll(
            @RequestParam(required = false) Integer lancamentoId) {

        List<RecebimentoDTO> body = (lancamentoId != null)
                ? service.findAllByLancamento(Long.valueOf(lancamentoId)) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

}
