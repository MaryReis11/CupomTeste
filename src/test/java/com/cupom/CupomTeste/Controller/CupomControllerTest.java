package com.cupom.CupomTeste.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.cupom.CupomTeste.model.Status.Status;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;
import com.cupom.CupomTeste.Service.CupomService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CupomController.class)
public class CupomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CupomService cupomService;

    @Test
    void testCreateCupom() throws Exception {
        UUID id = UUID.randomUUID();
        OffsetDateTime expiration = OffsetDateTime.now().plusDays(1);

        CupomResponse response = new CupomResponse(
            id,
            "ABC123",
            "Cupom de teste",
            new BigDecimal("1.0"),
            expiration,
            Status.ACTIVE,
            false,
            false
        );

        when(cupomService.createCupom(any(CupomRequest.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(response));

        String jsonRequest = """
            {
                "code": "ABC123",
                "description": "Cupom de teste",
                "discountValue": 1.0,
                "expirationDate": "%s",
                "published": false
            }
        """.formatted(expiration);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.description").value("Cupom de teste"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetCupomById() throws Exception {
        UUID id = UUID.randomUUID();
        OffsetDateTime expiration = OffsetDateTime.now().plusDays(1);

        CupomResponse response = new CupomResponse(
            id,
            "XYZ789",
            "Outro cupom",
            new BigDecimal("2.5"),
            expiration,
            Status.ACTIVE,
            true,
            false
        );

        when(cupomService.getCupomById(id))
            .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("XYZ789"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void testDeleteCupom() throws Exception {
        UUID id = UUID.randomUUID();

        // Simula retorno HTTP 204
        when(cupomService.deleteCupom(id))
            .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());
    }
}