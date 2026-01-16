package com.cupom.CupomTeste.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cupom.CupomTeste.Exception.NegocioException;
import com.cupom.CupomTeste.Exception.TabelaDeErros;
import com.cupom.CupomTeste.Repository.CupomRepository;
import com.cupom.CupomTeste.mapper.CupomMapper;
import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;
import com.cupom.CupomTeste.model.dto.CupomRequest;
import com.cupom.CupomTeste.model.dto.CupomResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da classe CupomService")
class CupomServiceTest {

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private CupomMapper mapper;

    @InjectMocks
    private CupomService cupomService;

    private UUID cupomId;
    private Cupom cupom;
    private CupomRequest cupomRequest;
    private CupomResponse cupomResponse;

    @BeforeEach
    void setUp() {
        cupomId = UUID.randomUUID();
        
        cupom = new Cupom();
        cupom.setCode("ABC123");
        cupom.setDescription("Cupom de Teste");
        cupom.setDiscountValue(BigDecimal.valueOf(10.0));
        cupom.setExpirationDate(LocalDateTime.now().plusDays(30));
        cupom.setStatus(Status.ACTIVE);
        cupom.setPublished(true);

        cupomRequest = new CupomRequest("ABC123", "Cupom de Teste", BigDecimal.valueOf(10.0), 
                                        LocalDateTime.now().plusDays(30), true);

        cupomResponse = new CupomResponse(cupomId, "ABC123", "Cupom de Teste", BigDecimal.valueOf(10.0), 
                                          LocalDateTime.now().plusDays(30), Status.ACTIVE, true, false);
    }

    @Test
    @DisplayName("Deve criar um cupom com sucesso")
    void testCreateCupomSuccess() {
        // Arrange
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);
        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);
        when(mapper.toResponse(cupom)).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ABC123", response.getBody().getCode());
        assertEquals(Status.ACTIVE, response.getBody().getStatus());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com código inválido")
    void testCreateCupomWithInvalidCode() {
        // Arrange
        cupom.setCode("AB");
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.CODIGO_INVALIDO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com código contendo caracteres especiais e tamanho inválido")
    void testCreateCupomWithSpecialCharactersInCode() {
        // Arrange
        cupom.setCode("A@B#C");
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.CODIGO_INVALIDO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve limpar caracteres especiais do código e aceitar se resultado for válido")
    void testCreateCupomWithCodeCleanup() {
        // Arrange
        Cupom cupomComCaracteresEspeciais = new Cupom();
        cupomComCaracteresEspeciais.setCode("A-B-C-1-2-3");
        cupomComCaracteresEspeciais.setDescription("Teste");
        cupomComCaracteresEspeciais.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomComCaracteresEspeciais.setExpirationDate(LocalDateTime.now().plusDays(30));
        cupomComCaracteresEspeciais.setPublished(true);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomComCaracteresEspeciais);
        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupomComCaracteresEspeciais);
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ABC123", cupomComCaracteresEspeciais.getCode());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com valor de desconto inválido")
    void testCreateCupomWithInvalidDiscountValue() {
        // Arrange
        cupom.setDiscountValue(BigDecimal.valueOf(0.3));
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.VALOR_DESCONTO_INVALIDO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com data de expiração inválida")
    void testCreateCupomWithExpiredDate() {
        // Arrange
        cupom.setExpirationDate(LocalDateTime.now().minusDays(1));
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.DATA_EXPIRACAO_INVALIDA, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve retornar todos os cupons com sucesso")
    void testGetAllCupoms() {
        // Arrange
        List<Cupom> cupoms = List.of(cupom);
        List<CupomResponse> responses = List.of(cupomResponse);
        
        when(cupomRepository.findAll()).thenReturn(cupoms);
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<List<CupomResponse>> response = cupomService.getAllCupoms();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ABC123", response.getBody().get(0).getCode());
        verify(cupomRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver cupons")
    void testGetAllCupomsEmpty() {
        // Arrange
        when(cupomRepository.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<CupomResponse>> response = cupomService.getAllCupoms();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(cupomRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar cupom por id com sucesso")
    void testGetCupomById() {
        // Arrange
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.of(cupom));
        when(mapper.toResponse(cupom)).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.getCupomById(cupomId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ABC123", response.getBody().getCode());
        verify(cupomRepository, times(1)).findById(cupomId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cupom inexistente")
    void testGetCupomByIdNotFound() {
        // Arrange
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.empty());

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.getCupomById(cupomId));
        
        assertEquals(TabelaDeErros.CUPOM_NAO_ENCONTRADO, exception.getErro());
        verify(cupomRepository, times(1)).findById(cupomId);
    }

    @Test
    @DisplayName("Deve deletar cupom com sucesso")
    void testDeleteCupomSuccess() {
        // Arrange
        cupom.setStatus(Status.ACTIVE);
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.of(cupom));
        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);

        // Act
        ResponseEntity<Void> response = cupomService.deleteCupom(cupomId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(Status.DELETED, cupom.getStatus());
        verify(cupomRepository, times(1)).findById(cupomId);
        verify(cupomRepository, times(1)).save(cupom);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar cupom já deletado")
    void testDeleteCupomAlreadyDeleted() {
        // Arrange
        cupom.setStatus(Status.DELETED);
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.of(cupom));

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.deleteCupom(cupomId));
        
        assertEquals(TabelaDeErros.CUPOM_JA_DELETADO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar cupom inexistente")
    void testDeleteCupomNotFound() {
        // Arrange
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.empty());

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.deleteCupom(cupomId));
        
        assertEquals(TabelaDeErros.CUPOM_NAO_ENCONTRADO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve aceitar cupom com valor de desconto exatamente 0.5")
    void testCreateCupomWithMinimumValidDiscountValue() {
        // Arrange
        cupom.setDiscountValue(BigDecimal.valueOf(0.5));
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);
        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);
        when(mapper.toResponse(cupom)).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve aceitar cupom com data de expiração hoje")
    void testCreateCupomWithExpirationDateToday() {
        // Arrange
        cupom.setExpirationDate(LocalDateTime.now());
        when(mapper.toEntity(cupomRequest)).thenReturn(cupom);
        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);
        when(mapper.toResponse(cupom)).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }
}
