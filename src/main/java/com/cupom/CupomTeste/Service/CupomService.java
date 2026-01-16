package com.cupom.CupomTeste.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cupom.CupomTeste.Exception.NegocioException;
import com.cupom.CupomTeste.Exception.TabelaDeErros;
import com.cupom.CupomTeste.Repository.CupomRepository;
import com.cupom.CupomTeste.mapper.CupomMapper;
import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;

@Service
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CupomMapper mapper;

    public ResponseEntity<CupomResponse> createCupom(CupomRequest cupomRequest) {
        Cupom cupom = mapper.toEntity(cupomRequest);
        validarDadosCupom(cupom);
        cupom.setStatus(Status.ACTIVE); // status padrão
        Cupom salvo = cupomRepository.save(cupom);
        return ResponseEntity.status(201)
                             .body(mapper.toResponse(salvo));
    }

    public ResponseEntity<List<CupomResponse>> getAllCupoms() {
        List<Cupom> cupoms = cupomRepository.findAll();
        List<CupomResponse> responses = cupoms.stream()
                                              .map(mapper::toResponse)
                                              .toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<CupomResponse> getCupomById(UUID id) {
        Cupom cupom = validarCupom(id);
        return ResponseEntity.ok(mapper.toResponse(cupom));
    }

    public ResponseEntity<Void> deleteCupom(UUID id) {
        Cupom cupom = validarCupom(id);

        if (cupom.getStatus() == Status.DELETED) {
            throw new NegocioException(TabelaDeErros.CUPOM_JA_DELETADO);
        }

        cupom.setStatus(Status.DELETED);
        cupomRepository.save(cupom);
        return ResponseEntity.noContent().build();
    }

    private void validarDadosCupom(Cupom cupom) {
        String sanitizedCode = cupom.getCode().replaceAll("[^a-zA-Z0-9]", "");
        if (sanitizedCode.length() != 6) {
            throw new NegocioException(TabelaDeErros.CODIGO_INVALIDO);
        }
        cupom.setCode(sanitizedCode);

        if (cupom.getDiscountValue().compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new NegocioException(TabelaDeErros.VALOR_DESCONTO_INVALIDO);
        }

        OffsetDateTime expiration = cupom.getExpirationDate().atOffset(ZoneOffset.UTC);
        if (expiration.isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            throw new NegocioException(TabelaDeErros.DATA_EXPIRACAO_INVALIDA);
        }
    }

    private Cupom validarCupom(UUID id) {
        return cupomRepository.findById(id)
                .orElseThrow(() -> new NegocioException(TabelaDeErros.CUPOM_NAO_ENCONTRADO));
    }
}