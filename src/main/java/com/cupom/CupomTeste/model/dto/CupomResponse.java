package com.cupom.CupomTeste.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cupom.CupomTeste.model.Status.Status;

public class CupomResponse {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private Status status;
    private boolean published;
    private boolean redeemed;
	
	public CupomResponse(UUID cupomId, String string, String string2, BigDecimal valueOf, LocalDateTime plusDays,
			Status active, boolean b, boolean c) {
		// TODO Auto-generated constructor stub
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public boolean isRedeemed() {
		return redeemed;
	}
	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}
}