package com.cupom.CupomTeste.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "coupons")
public class Cupom {
	
	@Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean deleted;

    protected Cupom() {
    }

    public Cupom(String code,
                 String description,
                 BigDecimal discountValue,
                 LocalDate expirationDate,
                 boolean published) {

        this.code = sanitizeCode(code);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.deleted = false;

        validate();
    }

    private void validate() {
        if (discountValue.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new RuntimeException("Discount value must be at least 0.5");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Expiration date cannot be in the past");
        }
    }

    private String sanitizeCode(String rawCode) {
        if (rawCode == null) {
            throw new RuntimeException("Code is required");
        }

        String sanitized = rawCode.replaceAll("[^a-zA-Z0-9]", "");

        if (sanitized.length() != 6) {
            throw new RuntimeException("Coupon code must have exactly 6 characters");
        }

        return sanitized;
    }

    // ✅ regra de negócio
    public void deletar() {
        if (this.deleted) {
            throw new RuntimeException("Coupon already deleted");
        }
        this.deleted = true;
    }
    
    public boolean isPublished() {
        return published;
    }

    public boolean isDeleted() {
        return deleted;
    }
    // 👇 Getters manuais(o ideal seria a notação para ficar mais limpo, mas parece que estou com problema de versão e não consigo arrumar
    //a tempo de terminar o teste
    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean getPublished() {
        return published;
    }

    public boolean getDeleted() {
        return deleted;
    }

}
