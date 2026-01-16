package com.cupom.CupomTeste.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

	 @Autowired
	    CupomService cupomService;

	    @PostMapping
	    public ResponseEntity<CupomResponse> createCupom(@RequestBody CupomRequest cupomRequest) {
	        return cupomService.createCupom(cupomRequest);
	    }

	    @GetMapping("{id}")
	    public ResponseEntity<CupomResponse> getCupomById(@PathVariable UUID id) {
	        return cupomService.getCupomById(id);
	    }

	    @DeleteMapping("{id}")
	    public ResponseEntity<Void> deleteCupom(@PathVariable UUID id) {
	        cupomService.deleteCupom(id);
	        return ResponseEntity.noContent().build();
	    }
}