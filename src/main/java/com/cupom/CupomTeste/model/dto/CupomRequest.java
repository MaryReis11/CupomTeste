package com.cupom.CupomTeste.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CupomRequest (

	 String code,
	 String description,
	 BigDecimal discountValue,
	 LocalDate expirationDate,
	 boolean published
) {}
	   