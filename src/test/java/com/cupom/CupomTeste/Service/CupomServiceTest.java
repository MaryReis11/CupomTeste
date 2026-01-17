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
    private LocalDateTime expirationDate;

    @BeforeEach
    void setUp() {
        cupomId = UUID.randomUUID();
        expirationDate = LocalDateTime.now().plusDays(30);
        
        cupom = new Cupom();
        cupom.setId(cupomId);
        cupom.setCode("ABC123");
        cupom.setDescription("Cupom de Teste");
        cupom.setDiscountValue(BigDecimal.valueOf(10.0));
        cupom.setExpirationDate(expirationDate);
        cupom.setStatus(Status.ACTIVE);
        cupom.setPublished(true);
        cupom.setRedeemed(false);

        cupomRequest = new CupomRequest();
        cupomRequest.setCode("ABC123");
        cupomRequest.setDescription("Cupom de Teste");
        cupomRequest.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomRequest.setExpirationDate(expirationDate);
        cupomRequest.setPublished(true);

        cupomResponse = new CupomResponse(cupomId, "ABC123", "Cupom de Teste", BigDecimal.valueOf(10.0), 
                                          expirationDate, Status.ACTIVE, true, false);
    }

    @Test
    @DisplayName("Deve criar um cupom com sucesso e retornar 201")
    void testCreateCupomSuccess() {
        // Arrange
        Cupom cupomEntrada = new Cupom();
        cupomEntrada.setCode("ABC123");
        cupomEntrada.setDescription("Cupom de Teste");
        cupomEntrada.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomEntrada.setExpirationDate(expirationDate);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomEntrada);
        when(cupomRepository.save(any(Cupom.class))).thenAnswer(invocation -> {
            Cupom saved = invocation.getArgument(0);
            saved.setId(cupomId);
            saved.setStatus(Status.ACTIVE);
            return saved;
        });
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com código inválido")
    void testCreateCupomWithInvalidCode() {
        // Arrange
        Cupom cupomInvalido = new Cupom();
        cupomInvalido.setCode("AB");
        cupomInvalido.setDescription("Cupom de Teste");
        cupomInvalido.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomInvalido.setExpirationDate(expirationDate);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomInvalido);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.CODIGO_INVALIDO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com código contendo caracteres especiais")
    void testCreateCupomWithSpecialCharactersInCode() {
        // Arrange
        Cupom cupomInvalido = new Cupom();
        cupomInvalido.setCode("A@B#C");
        cupomInvalido.setDescription("Cupom de Teste");
        cupomInvalido.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomInvalido.setExpirationDate(expirationDate);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomInvalido);

        // Act & Assert
        NegocioException exception = assertThrows(NegocioException.class, 
            () -> cupomService.createCupom(cupomRequest));
        
        assertEquals(TabelaDeErros.CODIGO_INVALIDO, exception.getErro());
        verify(cupomRepository, never()).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve limpar caracteres especiais do código")
    void testCreateCupomWithCodeCleanup() {
        // Arrange
        Cupom cupomComCaracteresEspeciais = new Cupom();
        cupomComCaracteresEspeciais.setCode("A-B-C-1-2-3");
        cupomComCaracteresEspeciais.setDescription("Teste");
        cupomComCaracteresEspeciais.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomComCaracteresEspeciais.setExpirationDate(expirationDate);
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
        Cupom cupomInvalido = new Cupom();
        cupomInvalido.setCode("ABC123");
        cupomInvalido.setDescription("Cupom de Teste");
        cupomInvalido.setDiscountValue(BigDecimal.valueOf(0.3));
        cupomInvalido.setExpirationDate(expirationDate);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomInvalido);

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
        Cupom cupomInvalido = new Cupom();
        cupomInvalido.setCode("ABC123");
        cupomInvalido.setDescription("Cupom de Teste");
        cupomInvalido.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomInvalido.setExpirationDate(LocalDateTime.now().minusDays(1));
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomInvalido);

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
        
        when(cupomRepository.findAll()).thenReturn(cupoms);
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<List<CupomResponse>> response = cupomService.getAllCupoms();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
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
        Cupom cupomFound = new Cupom();
        cupomFound.setId(cupomId);
        cupomFound.setCode("ABC123");
        cupomFound.setDescription("Cupom de Teste");
        cupomFound.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomFound.setExpirationDate(expirationDate);
        cupomFound.setStatus(Status.ACTIVE);
        cupomFound.setPublished(true);
        
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.of(cupomFound));
        when(mapper.toResponse(cupomFound)).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.getCupomById(cupomId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
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
        Cupom cupomDeleted = new Cupom();
        cupomDeleted.setId(cupomId);
        cupomDeleted.setCode("ABC123");
        cupomDeleted.setStatus(Status.DELETED);
        
        when(cupomRepository.findById(cupomId)).thenReturn(Optional.of(cupomDeleted));

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
        Cupom cupomEntrada = new Cupom();
        cupomEntrada.setCode("ABC123");
        cupomEntrada.setDescription("Cupom de Teste");
        cupomEntrada.setDiscountValue(BigDecimal.valueOf(0.5));
        cupomEntrada.setExpirationDate(expirationDate);
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomEntrada);
        when(cupomRepository.save(any(Cupom.class))).thenAnswer(invocation -> {
            Cupom saved = invocation.getArgument(0);
            saved.setId(cupomId);
            saved.setStatus(Status.ACTIVE);
            return saved;
        });
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }

    @Test
    @DisplayName("Deve aceitar cupom com data de expiração no futuro")
    void testCreateCupomWithExpirationDateToday() {
        // Arrange
        Cupom cupomEntrada = new Cupom();
        cupomEntrada.setCode("ABC123");
        cupomEntrada.setDescription("Cupom de Teste");
        cupomEntrada.setDiscountValue(BigDecimal.valueOf(10.0));
        cupomEntrada.setExpirationDate(LocalDateTime.now().plusDays(1)); // Um dia no futuro
        
        when(mapper.toEntity(cupomRequest)).thenReturn(cupomEntrada);
        when(cupomRepository.save(any(Cupom.class))).thenAnswer(invocation -> {
            Cupom saved = invocation.getArgument(0);
            saved.setId(cupomId);
            saved.setStatus(Status.ACTIVE);
            return saved;
        });
        when(mapper.toResponse(any(Cupom.class))).thenReturn(cupomResponse);

        // Act
        ResponseEntity<CupomResponse> response = cupomService.createCupom(cupomRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cupomRepository, times(1)).save(any(Cupom.class));
    }
}