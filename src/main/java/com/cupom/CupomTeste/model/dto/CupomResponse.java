package com.cupom.CupomTeste.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.cupom.CupomTeste.model.Cupom;


public record CupomResponse (

		UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDate expirationDate,
        String status,
        boolean published,
        boolean redeemed
) {

    public static CupomResponse from(Cupom cupom) {
        return new CupomResponse(
                cupom.getId(),
                cupom.getCode(),
                cupom.getDescription(),
                cupom.getDiscountValue(),
                cupom.getExpirationDate(),
                cupom.isDeleted() ? "DELETED" : "ACTIVE",
                cupom.isPublished(),
                false // contrato pede, mas domínio ainda não trata
        );
    }
}