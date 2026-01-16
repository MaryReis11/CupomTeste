package com.cupom.CupomTeste.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public record CupomResponse (

		UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        Status status,
        boolean published,
        boolean redeemed
) {}