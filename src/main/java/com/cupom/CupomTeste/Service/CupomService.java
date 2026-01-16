package com.cupom.CupomTeste.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cupom.CupomTeste.Repository.CupomRepository;
import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.dto.CupomRequest;

@Service
public class CupomService {

	private final CupomRepository cupomRepository;

    public CupomService(CupomRepository cupomRepository) {
        this.cupomRepository = cupomRepository;
    }

    public Cupom criar(CupomRequest request) {
        Cupom cupom = new Cupom(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        return cupomRepository.save(cupom);
    }

    public Cupom buscarPorId(UUID id) {
        return cupomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    }

    public void deletar(UUID id) {
        Cupom cupom = buscarPorId(id);
        cupom.deletar(); // regra no domínio
        cupomRepository.save(cupom);
    }
}