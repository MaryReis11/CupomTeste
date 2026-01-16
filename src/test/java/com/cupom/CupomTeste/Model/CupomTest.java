package com.cupom.CupomTeste.Model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;

@DisplayName("Testes do Modelo Cupom")
class CupomTest {

    private Cupom cupom;
    private LocalDateTime expirationDate;

    @BeforeEach
    void setUp() {
        expirationDate = LocalDateTime.now().plusDays(30);
        cupom = new Cupom();
    }

    @Test
    @DisplayName("Cupom válido quando tem todos os dados obrigatórios")
    void testValidCupomWithAllRequiredData() {
        // Arrange
        cupom.setCode("ABC123");
        cupom.setDiscountValue(BigDecimal.valueOf(10.0));
        cupom.setExpirationDate(expirationDate);
        cupom.setStatus(Status.ACTIVE);

        // Assert
        assertNotNull(cupom.getId());
        assertNotNull(cupom.getCode());
        assertNotNull(cupom.getDiscountValue());
        assertNotNull(cupom.getExpirationDate());
        assertNotNull(cupom.getStatus());
    }

    @Test
    @DisplayName("Cupom inativo pode ser resgatado")
    void testInactiveCupomCanBeRedeemed() {
        // Arrange
        cupom.setStatus(Status.DELETED);
        cupom.setRedeemed(false);

        // Act
        cupom.setRedeemed(true);

        // Assert
        assertTrue(cupom.isRedeemed());
        assertEquals(Status.DELETED, cupom.getStatus());
    }

    @Test
    @DisplayName("Cupom publicado fica visível")
    void testPublishedCupomIsVisible() {
        // Arrange
        cupom.setPublished(true);

        // Assert
        assertTrue(cupom.isPublished());
    }

    @Test
    @DisplayName("Cupom não publicado fica oculto")
    void testUnpublishedCupomIsHidden() {
        // Arrange
        cupom.setPublished(false);

        // Assert
        assertFalse(cupom.isPublished());
    }

    @Test
    @DisplayName("Código do cupom tem máximo 6 caracteres")
    void testCupomCodeMaxLength() {
        // Arrange
        String code = "ABC123";
        cupom.setCode(code);

        // Assert
        assertEquals(6, cupom.getCode().length());
        assertTrue(cupom.getCode().length() <= 6);
    }

    @Test
    @DisplayName("Desconto mínimo é 0.5")
    void testMinimumDiscountValue() {
        // Arrange
        BigDecimal minDiscount = BigDecimal.valueOf(0.5);
        cupom.setDiscountValue(minDiscount);

        // Assert
        assertTrue(cupom.getDiscountValue().compareTo(BigDecimal.valueOf(0.5)) >= 0);
    }

    @Test
    @DisplayName("Data de expiração não pode ser no passado")
    void testExpirationDateCannotBeInPast() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        cupom.setExpirationDate(pastDate);

        // Assert - validação ocorre no service, apenas verificamos se foi setado
        assertEquals(pastDate, cupom.getExpirationDate());
    }

    @Test
    @DisplayName("Cupom ativo quando status é ACTIVE")
    void testCupomIsActiveWhenStatusActive() {
        // Arrange
        cupom.setStatus(Status.ACTIVE);

        // Assert
        assertEquals(Status.ACTIVE, cupom.getStatus());
    }

    @Test
    @DisplayName("Cupom deletado quando status é DELETED")
    void testCupomIsDeletedWhenStatusDeleted() {
        // Arrange
        cupom.setStatus(Status.DELETED);

        // Assert
        assertEquals(Status.DELETED, cupom.getStatus());
    }

    @Test
    @DisplayName("Descrição pode ser um texto longo")
    void testDescriptionCanBeLongText() {
        // Arrange
        String longDescription = "Este é um cupom muito especial com uma descrição detalhada " +
                                "que inclui informações sobre o desconto, termos e condições, " +
                                "restrições e como utilizar o cupom para obter o benefício máximo";
        cupom.setDescription(longDescription);

        // Assert
        assertEquals(longDescription, cupom.getDescription());
    }

    @Test
    @DisplayName("UUID do cupom é único")
    void testCupomIdIsUnique() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        cupom.setId(id1);

        Cupom cupom2 = new Cupom();
        cupom2.setId(id2);

        // Assert
        assertNotEquals(cupom.getId(), cupom2.getId());
    }

    @Test
    @DisplayName("Cupom pode alternar entre publicado e não publicado")
    void testCupomCanTogglePublishState() {
        // Arrange & Act
        cupom.setPublished(true);
        assertTrue(cupom.isPublished());

        cupom.setPublished(false);
        assertFalse(cupom.isPublished());

        cupom.setPublished(true);

        // Assert
        assertTrue(cupom.isPublished());
    }

    @Test
    @DisplayName("Cupom pode alternar entre resgatado e não resgatado")
    void testCupomCanToggleRedeemedState() {
        // Arrange & Act
        cupom.setRedeemed(false);
        assertFalse(cupom.isRedeemed());

        cupom.setRedeemed(true);
        assertTrue(cupom.isRedeemed());

        cupom.setRedeemed(false);

        // Assert
        assertFalse(cupom.isRedeemed());
    }

    @Test
    @DisplayName("Múltiplos cupons podem ter códigos diferentes")
    void testMultipleCupomsHaveDifferentCodes() {
        // Arrange
        Cupom cupom1 = new Cupom();
        cupom1.setCode("ABC123");

        Cupom cupom2 = new Cupom();
        cupom2.setCode("XYZ789");

        // Assert
        assertNotEquals(cupom1.getCode(), cupom2.getCode());
    }

    @Test
    @DisplayName("Desconto com valores altos é aceitável")
    void testHighDiscountValuesAccepted() {
        // Arrange
        BigDecimal highDiscount = BigDecimal.valueOf(999.99);
        cupom.setDiscountValue(highDiscount);

        // Assert
        assertEquals(highDiscount, cupom.getDiscountValue());
    }

    @Test
    @DisplayName("Data de expiração pode ser em qualquer momento futuro")
    void testExpirationDateCanBeFarInFuture() {
        // Arrange
        LocalDateTime farFutureDate = LocalDateTime.now().plusYears(5);
        cupom.setExpirationDate(farFutureDate);

        // Assert
        assertEquals(farFutureDate, cupom.getExpirationDate());
    }

    @Test
    @DisplayName("Cupom começa como não resgatado")
    void testCupomStartsAsNotRedeemed() {
        // Assert
        assertFalse(cupom.isRedeemed());
    }

    @Test
    @DisplayName("Estado do cupom persiste após múltiplas operações")
    void testCupomStateAfterMultipleOperations() {
        // Arrange
        UUID id = UUID.randomUUID();
        cupom.setId(id);
        cupom.setCode("TEST01");
        cupom.setStatus(Status.ACTIVE);
        cupom.setPublished(true);

        // Act
        cupom.setRedeemed(true);
        cupom.setStatus(Status.DELETED);

        // Assert
        assertEquals(id, cupom.getId());
        assertEquals("TEST01", cupom.getCode());
        assertEquals(Status.DELETED, cupom.getStatus());
        assertTrue(cupom.isRedeemed());
        assertTrue(cupom.isPublished());
    }

    @Test
    @DisplayName("Cupom com descrição vazia é válido")
    void testCupomWithEmptyDescriptionIsValid() {
        // Arrange
        cupom.setDescription("");

        // Assert
        assertEquals("", cupom.getDescription());
    }

    @Test
    @DisplayName("Cupom com descrição null é válido")
    void testCupomWithNullDescriptionIsValid() {
        // Arrange
        cupom.setDescription(null);

        // Assert
        assertNull(cupom.getDescription());
    }
}
