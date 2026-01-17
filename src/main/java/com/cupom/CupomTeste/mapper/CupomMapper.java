package com.cupom.CupomTeste.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;

@Mapper(componentModel = "spring")
public interface CupomMapper {

    CupomResponse toResponse(Cupom cupom);

    Cupom toEntity(CupomRequest cupomRequest);

    // Converter OffsetDateTime -> LocalDateTime
    default LocalDateTime map(OffsetDateTime value) {
        if (value == null) return null;
        return value.toLocalDateTime();
    }

    // Converter LocalDateTime -> OffsetDateTime se precisar do inverso
    default OffsetDateTime map(LocalDateTime value) {
        if (value == null) return null;
        return value.atOffset(java.time.ZoneOffset.UTC);
    }

    default String map(Status status) {
        if (status == null) return null;
        return status.name();
    }

    default Status map(String status) {
        if (status == null) return null;
        return Status.valueOf(status);
    }
}