package com.projeto.resources;

import com.projeto.domains.dtos.FaturaCartaoDTO;
import com.projeto.services.FaturaCartaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cartoes/{id}/faturas")
public class FaturaCartaoResource {

    private final FaturaCartaoService service;

    public FaturaCartaoResource(FaturaCartaoService service) {
        this.service = service;
    }

    // GET paginado; filtro por grupo opcional (?cartaoId=)
    @GetMapping
    public ResponseEntity<Page<FaturaCartaoDTO>> list(
            @RequestParam(required = false) Integer cartaoId,
            @PageableDefault(size = 20, sort = "dataFechamento") Pageable pageable) {

        Page<FaturaCartaoDTO> page = (cartaoId != null)
                ? service.findAllByCartao(cartaoId, pageable) // paginado + filtro
                : service.findAll(pageable);                // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por grupo opcional (?cartaoId=)
    @GetMapping("/all")
    public ResponseEntity<List<FaturaCartaoDTO>> listAll(
            @RequestParam(required = false) Integer cartaoId) {

        List<FaturaCartaoDTO> body = (cartaoId != null)
                ? service.findAllByCartao(cartaoId) // não paginado + filtro
                : service.findAll();              // não paginado sem filtro

        return ResponseEntity.ok(body);
    }
    
}
