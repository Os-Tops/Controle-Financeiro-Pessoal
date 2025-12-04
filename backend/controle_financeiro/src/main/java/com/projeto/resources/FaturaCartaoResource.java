package com.projeto.resources;

import com.projeto.domains.dtos.FaturaCartaoDTO;
import com.projeto.services.FaturaCartaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/cartoes/{cartaoId}/faturas")
public class FaturaCartaoResource {

    private final FaturaCartaoService service;

    public FaturaCartaoResource(FaturaCartaoService service) {
        this.service = service;
    }

    // GET /api/v1/cartoes/{cartaoId}/faturas  -> paginado
    @GetMapping
    public ResponseEntity<Page<FaturaCartaoDTO>> list(
            @PathVariable Integer cartaoId,
            @PageableDefault(size = 20, sort = "dataFechamento") Pageable pageable) {

        Page<FaturaCartaoDTO> page = service.findAllByCartao(cartaoId, pageable);
        return ResponseEntity.ok(page);
    }

    // GET /api/v1/cartoes/{cartaoId}/faturas/all  -> não paginado
    @GetMapping("/all")
    public ResponseEntity<List<FaturaCartaoDTO>> listAll(
            @PathVariable Integer cartaoId) {

        List<FaturaCartaoDTO> body = service.findAllByCartao(cartaoId);
        return ResponseEntity.ok(body);
    }

    // POST /api/v1/cartoes/{cartaoId}/faturas/fechamento  -> fecha fatura
    @PostMapping("/fechamento")
    public ResponseEntity<Void> fecharFatura(@PathVariable Integer cartaoId) {
        service.fecharFatura(cartaoId);
        return ResponseEntity.noContent().build();
    }

    // POST /api/v1/cartoes/{cartaoId}/faturas/{faturaId}/pagar  -> paga fatura
    @PostMapping("/{faturaId}/pagar")
    public ResponseEntity<Void> pagarFatura(@PathVariable Integer cartaoId,
                                            @PathVariable Long faturaId) {
        service.pagarFatura(cartaoId, faturaId);
        return ResponseEntity.noContent().build();
    }
}
