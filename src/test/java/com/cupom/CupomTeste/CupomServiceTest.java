package com.cupom.CupomTeste;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cupom.CupomTeste.Repository.CupomRepository;
import com.cupom.CupomTeste.Service.CupomService;
import com.cupom.CupomTeste.mapper.CupomMapper;
import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;

public class CupomServiceTest {

	@InjectMocks
    private CupomService cupomService;

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private CupomMapper mapper;

    private CupomRequest request;
    private Cupom cupom;
    private CupomResponse response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Criando um request válido
        request = new CupomRequest(
        );

        // Entidade Cupom correspondente
        cupom = new Cupom(
        );
        cupom.setStatus(Status.ACTIVE);

        // Resposta mapeada
        response = new CupomResponse(
                UUID.randomUUID(),
                cupom.getCode(),
                cupom.getDescription(),
                cupom.getDiscountValue(),
                cupom.getExpirationDate(),
                cupom.getStatus(),
                cupom.isPublished(),
                false
        );
    }

    @Test
    void testCreateCupom_Success() {
        when(mapper.toEntity(request)).thenReturn(cupom);
        when(cupomRepository.save(cupom)).thenReturn(cupom);
        when(mapper.toResponse(cupom)).thenReturn(response);

        ResponseEntity<CupomResponse> result = cupomService.createCupom(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(cupomRepository, times(1)).save(cupom);
    }

    @Test
    void testGetAllCupoms() {
        List<Cupom> cupoms = List.of(cupom);
        List<CupomResponse> responses = List.of(response);

        when(cupomRepository.findAll()).thenReturn(cupoms);
        when(mapper.toResponse(cupom)).thenReturn(response);

        ResponseEntity<List<CupomResponse>> result = cupomService.getAllCupoms();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
    }

    @Test
    void testGetCupomById_Success() {
        UUID id = UUID.randomUUID();
        cupom.setStatus(Status.ACTIVE);

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));
        when(mapper.toResponse(cupom)).thenReturn(response);

        ResponseEntity<CupomResponse> result = cupomService.getCupomById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteCupom_Success() {
        UUID id = UUID.randomUUID();
        cupom.setStatus(Status.ACTIVE);

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));
        when(cupomRepository.save(cupom)).thenReturn(cupom);

        ResponseEntity<Void> result = cupomService.deleteCupom(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(Status.DELETED, cupom.getStatus());

        verify(cupomRepository, times(1)).save(cupom);
    }

    @Test
    void testDeleteCupom_AlreadyDeleted() {
        UUID id = UUID.randomUUID();
        cupom.setStatus(Status.DELETED);

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cupomService.deleteCupom(id);
        });

        assertEquals("Cupom já está deletado.", ex.getMessage());
    }

    @Test
    void testCreateCupom_InvalidDiscount() {
        request = new CupomRequest(
                "ABC123",
                "Teste cupom",
                BigDecimal.valueOf(0.1), // desconto inválido
                LocalDateTime.now().plusDays(1),
                false
        );
        cupom = new Cupom(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        when(mapper.toEntity(request)).thenReturn(cupom);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cupomService.createCupom(request);
        });

        assertEquals("O valor de desconto do cupom deve ser no mínimo 0.5.", ex.getMessage());
    }
}
}
