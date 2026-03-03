package com.projeto.resources;

import com.projeto.domains.dtos.TelefoneUsuarioDTO;
import com.projeto.domains.dtos.TelefoneUsuarioDTO;
import com.projeto.services.TelefoneUsuarioService;
import com.projeto.services.TelefoneUsuarioService;
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
@RequestMapping("/api/v1/telefoneusuario")
public class TelefoneUsuarioResource {

    private final TelefoneUsuarioService service;

    public TelefoneUsuarioResource(TelefoneUsuarioService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TelefoneUsuarioDTO> findById(@PathVariable Integer id) {
        TelefoneUsuarioDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TelefoneUsuarioDTO> create(
            @RequestBody @Validated(TelefoneUsuarioDTO.Create.class) TelefoneUsuarioDTO dto) {

        TelefoneUsuarioDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TelefoneUsuarioDTO> update(@PathVariable Long id,
                                              @RequestBody @Validated(TelefoneUsuarioDTO.Update.class) TelefoneUsuarioDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
