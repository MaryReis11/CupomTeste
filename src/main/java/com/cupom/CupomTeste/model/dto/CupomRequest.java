package com.cupom.CupomTeste.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record CupomRequest (

		String code,
	    String description,
	    BigDecimal discountValue,
	    OffsetDateTime expirationDate, // <-- aqui
	    boolean published
) {}
	   