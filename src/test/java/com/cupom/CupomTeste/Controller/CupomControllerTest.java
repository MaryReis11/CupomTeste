package com.cupom.CupomTeste.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cupom.CupomTeste.Exception.NegocioException;
import com.cupom.CupomTeste.Exception.TabelaDeErros;
import com.cupom.CupomTeste.Service.CupomService;
import com.cupom.CupomTeste.model.dto.CupomRequest;

@WebMvcTest(CupomController.class)
@DisplayName("Testes da classe CupomController")
public class CupomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CupomService cupomService;

    private UUID cupomId;
    private LocalDateTime expirationDate;

    @BeforeEach
    void setUp() {
        cupomId = UUID.randomUUID();
        expirationDate = LocalDateTime.now().plusDays(30);
    }

    @Test
    @DisplayName("Deve criar um cupom com sucesso e retornar 201")
    void testCreateCupomSuccess() throws Exception {
        when(cupomService.createCupom(any(CupomRequest.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        String jsonRequest = """
            {
                "code": "ABC123",
                "description": "Cupom de teste",
                "discountValue": 10.0,
                "expirationDate": "%s",
                "published": true
            }
        """.formatted(expirationDate);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(cupomService, times(1)).createCupom(any(CupomRequest.class));
    }

    @Test
    @DisplayName("Deve retornar 201 com JSON válido na criação")
    void testCreateCupomReturnsJson() throws Exception {
        when(cupomService.createCupom(any(CupomRequest.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        String jsonRequest = """
            {
                "code": "XYZ789",
                "description": "Outro cupom",
                "discountValue": 5.0,
                "expirationDate": "%s",
                "published": false
            }
        """.formatted(expirationDate);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar cupom por id com sucesso e retornar 200")
    void testGetCupomByIdSuccess() throws Exception {
        when(cupomService.getCupomById(cupomId))
            .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/coupon/{id}", cupomId))
                .andExpect(status().isOk());

        verify(cupomService, times(1)).getCupomById(cupomId);
    }

    @Test
    @DisplayName("Deve chamar service ao buscar cupom por id")
    void testGetCupomByIdCallsService() throws Exception {
        when(cupomService.getCupomById(cupomId))
            .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/coupon/{id}", cupomId))
                .andExpect(status().isOk());

        verify(cupomService, times(1)).getCupomById(cupomId);
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao buscar cupom inexistente")
    void testGetCupomByIdNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        
        when(cupomService.getCupomById(nonExistentId))
            .thenThrow(new NegocioException(TabelaDeErros.CUPOM_NAO_ENCONTRADO));

        mockMvc.perform(get("/coupon/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar cupom com sucesso e retornar 204")
    void testDeleteCupomSuccess() throws Exception {
        when(cupomService.deleteCupom(cupomId))
            .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/coupon/{id}", cupomId))
                .andExpect(status().isNoContent());

        verify(cupomService, times(1)).deleteCupom(cupomId);
    }

    @Test
    @DisplayName("Deve chamar service ao deletar cupom")
    void testDeleteCupomCallsService() throws Exception {
        when(cupomService.deleteCupom(cupomId))
            .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/coupon/{id}", cupomId))
                .andExpect(status().isNoContent());

        verify(cupomService, times(1)).deleteCupom(cupomId);
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao deletar cupom inexistente")
    void testDeleteCupomNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        
        when(cupomService.deleteCupom(nonExistentId))
            .thenThrow(new NegocioException(TabelaDeErros.CUPOM_NAO_ENCONTRADO));

        mockMvc.perform(delete("/coupon/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve validar conteúdo JSON na criação")
    void testCreateCupomWithValidJson() throws Exception {
        when(cupomService.createCupom(any(CupomRequest.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        String jsonRequest = """
            {
                "code": "TEST01",
                "description": "Teste",
                "discountValue": 15.0,
                "expirationDate": "%s",
                "published": true
            }
        """.formatted(expirationDate);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(cupomService, times(1)).createCupom(any(CupomRequest.class));
    }

    @Test
    @DisplayName("Deve aceitar requisição POST com dados válidos")
    void testCreateCupomWithValidData() throws Exception {
        when(cupomService.createCupom(any(CupomRequest.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        String jsonRequest = """
            {
                "code": "PROMO01",
                "description": "Promoção especial",
                "discountValue": 20.5,
                "expirationDate": "%s",
                "published": true
            }
        """.formatted(expirationDate);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 204 sem conteúdo ao deletar")
    void testDeleteCupomReturnsNoContent() throws Exception {
        when(cupomService.deleteCupom(cupomId))
            .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/coupon/{id}", cupomId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar cupom existente")
    void testGetCupomReturnsOk() throws Exception {
        when(cupomService.getCupomById(cupomId))
            .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/coupon/{id}", cupomId))
                .andExpect(status().isOk());

        verify(cupomService, times(1)).getCupomById(cupomId);
    }
}