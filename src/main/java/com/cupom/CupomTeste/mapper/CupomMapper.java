package com.cupom.CupomTeste.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;

@Mapper(componentModel = "spring")
public interface CupomMapper {

    @Mapping(source = "status", target = "status") // MapStruct vai usar método custom se tipos forem diferentes
    CupomResponse toResponse(Cupom cupom);

    Cupom toEntity(CupomRequest cupomRequest);

    default LocalDateTime map(OffsetDateTime odt) {
        return odt == null ? null : odt.toLocalDateTime();
    }

    default OffsetDateTime map(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(ZoneOffset.UTC);
    }
    // Converte Status -> String
    default String map(Status status) {
        return status == null ? null : status.name();
    }

    // Converte String -> Status
    default Status map(String status) {
        return status == null ? null : Status.valueOf(status.toUpperCase());
    }
}