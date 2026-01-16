package com.cupom.CupomTeste.Controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cupom.CupomTeste.Service.CupomService;
import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;


@RestController
@RequestMapping("/coupon")
public class CupomController {

    private final CupomService service;

    public CupomController(CupomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CupomResponse> criar(@RequestBody CupomRequest request) {
        Cupom cupom = service.criar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CupomResponse.from(cupom));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CupomResponse> buscarPorId(@PathVariable UUID id) {
        Cupom cupom = service.buscarPorId(id);
        return ResponseEntity.ok(CupomResponse.from(cupom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}