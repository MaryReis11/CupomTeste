package com.cupom.CupomTeste.Repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.cupom.CupomTeste.model.Cupom;
import com.cupom.CupomTeste.model.Status.Status;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes da classe CupomRepository")
class CupomRepositoryTest {

    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Cupom cupom;
    private UUID cupomId;
    private LocalDateTime expirationDate;

    @BeforeEach
    void setUp() {
        cupomId = UUID.randomUUID();
        expirationDate = LocalDateTime.now().plusDays(30);
        
        cupom = new Cupom();
        cupom.setId(cupomId);
        cupom.setCode("ABC123");
        cupom.setDescription("Cupom de teste");
        cupom.setDiscountValue(BigDecimal.valueOf(10.0));
        cupom.setExpirationDate(expirationDate);
        cupom.setStatus(Status.ACTIVE);
        cupom.setPublished(true);
    }

    @Test
    @DisplayName("Deve salvar um cupom com sucesso")
    void testSaveCupom() {
        // Act
        Cupom savedCupom = cupomRepository.save(cupom);

        // Assert
        assertNotNull(savedCupom);
        assertNotNull(savedCupom.getId());
        assertEquals("ABC123", savedCupom.getCode());
        assertEquals(Status.ACTIVE, savedCupom.getStatus());
        assertEquals("Cupom de teste", savedCupom.getDescription());
    }

    @Test
    @DisplayName("Deve encontrar cupom por id")
    void testFindCupomById() {
        // Arrange
        Cupom savedCupom = cupomRepository.save(cupom);

        // Act
        Optional<Cupom> foundCupom = cupomRepository.findById(savedCupom.getId());

        // Assert
        assertTrue(foundCupom.isPresent());
        assertEquals("ABC123", foundCupom.get().getCode());
        assertEquals(BigDecimal.valueOf(10.0), foundCupom.get().getDiscountValue());
        assertEquals(Status.ACTIVE, foundCupom.get().getStatus());
    }

    @Test
    @DisplayName("Deve encontrar cupom por código")
    void testFindCupomByCode() {
        // Arrange
        cupomRepository.save(cupom);

        // Act
        Optional<Cupom> foundCupom = cupomRepository.findByCode("ABC123");

        // Assert
        assertTrue(foundCupom.isPresent());
        assertEquals("ABC123", foundCupom.get().getCode());
        assertEquals("Cupom de teste", foundCupom.get().getDescription());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cupom inexistente por código")
    void testFindCupomByCodeNotFound() {
        // Act
        Optional<Cupom> foundCupom = cupomRepository.findByCode("INVALIDO");

        // Assert
        assertTrue(foundCupom.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cupom inexistente por id")
    void testFindCupomByIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act
        Optional<Cupom> foundCupom = cupomRepository.findById(nonExistentId);

        // Assert
        assertTrue(foundCupom.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar todos os cupons salvos")
    void testFindAllCupoms() {
        // Arrange
        Cupom cupom2 = new Cupom();
        cupom2.setCode("XYZ789");
        cupom2.setDescription("Outro cupom");
        cupom2.setDiscountValue(BigDecimal.valueOf(20.0));
        cupom2.setExpirationDate(expirationDate.plusDays(15));
        cupom2.setStatus(Status.ACTIVE);
        cupom2.setPublished(false);

        cupomRepository.save(cupom);
        cupomRepository.save(cupom2);

        // Act
        List<Cupom> cupoms = cupomRepository.findAll();

        // Assert
        assertNotNull(cupoms);
        assertEquals(2, cupoms.size());
        assertTrue(cupoms.stream().anyMatch(c -> c.getCode().equals("ABC123")));
        assertTrue(cupoms.stream().anyMatch(c -> c.getCode().equals("XYZ789")));
    }

    @Test
    @DisplayName("Deve deletar cupom por id")
    void testDeleteCupomById() {
        // Arrange
        Cupom savedCupom = cupomRepository.save(cupom);
        UUID savedId = savedCupom.getId();

        // Act
        cupomRepository.deleteById(savedId);

        // Assert
        Optional<Cupom> deletedCupom = cupomRepository.findById(savedId);
        assertTrue(deletedCupom.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar cupom existente")
    void testUpdateCupom() {
        // Arrange
        Cupom savedCupom = cupomRepository.save(cupom);
        UUID savedId = savedCupom.getId();

        // Act
        savedCupom.setCode("UPDAT1");
        savedCupom.setDiscountValue(BigDecimal.valueOf(50.0));
        savedCupom.setDescription("Cupom atualizado");
        Cupom updatedCupom = cupomRepository.save(savedCupom);

        // Assert
        assertEquals("UPDAT1", updatedCupom.getCode());
        assertEquals(BigDecimal.valueOf(50.0), updatedCupom.getDiscountValue());
        assertEquals("Cupom atualizado", updatedCupom.getDescription());
        assertEquals(savedId, updatedCupom.getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver cupons")
    void testFindAllCupomsEmpty() {
        // Act
        List<Cupom> cupoms = cupomRepository.findAll();

        // Assert
        assertNotNull(cupoms);
        assertTrue(cupoms.isEmpty());
        assertEquals(0, cupoms.size());
    }

    @Test
    @DisplayName("Deve verificar persistência com flush e clear")
    void testCupomPersistenceWithFlushAndClear() {
        // Arrange
        LocalDateTime futureDate = LocalDateTime.now().plusDays(60);
        cupom.setExpirationDate(futureDate);

        // Act
        Cupom savedCupom = cupomRepository.save(cupom);
        testEntityManager.flush();
        testEntityManager.clear();
        
        Cupom retrievedCupom = cupomRepository.findById(savedCupom.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedCupom);
        assertEquals("ABC123", retrievedCupom.getCode());
        assertEquals("Cupom de teste", retrievedCupom.getDescription());
        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(retrievedCupom.getDiscountValue()));
        assertEquals(Status.ACTIVE, retrievedCupom.getStatus());
        assertTrue(retrievedCupom.isPublished());
    }

    @Test
    @DisplayName("Deve contar total de cupons salvos")
    void testCountCupoms() {
        // Arrange
        Cupom cupom2 = new Cupom();
        cupom2.setCode("CUNT01");
        cupom2.setDescription("Para contar");
        cupom2.setDiscountValue(BigDecimal.valueOf(5.0));
        cupom2.setExpirationDate(expirationDate.plusDays(10));
        cupom2.setStatus(Status.ACTIVE);
        cupom2.setPublished(true);

        cupomRepository.save(cupom);
        cupomRepository.save(cupom2);

        // Act
        long count = cupomRepository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Deve verificar existência de cupom por id")
    void testExistsByIdCupom() {
        // Arrange
        Cupom savedCupom = cupomRepository.save(cupom);

        // Act
        boolean exists = cupomRepository.existsById(savedCupom.getId());
        boolean notExists = cupomRepository.existsById(UUID.randomUUID());

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("Deve salvar cupom com status DELETED")
    void testSaveCupomWithDeletedStatus() {
        // Arrange
        cupom.setStatus(Status.DELETED);

        // Act
        Cupom savedCupom = cupomRepository.save(cupom);

        // Assert
        assertNotNull(savedCupom);
        assertEquals(Status.DELETED, savedCupom.getStatus());
    }

    @Test
    @DisplayName("Deve salvar múltiplos cupons e recuperar todos")
    void testSaveMultipleCupoms() {
        // Arrange
        Cupom cupom2 = new Cupom();
        cupom2.setCode("MULT01");
        cupom2.setDescription("Múltiplo 1");
        cupom2.setDiscountValue(BigDecimal.valueOf(15.0));
        cupom2.setExpirationDate(expirationDate);
        cupom2.setStatus(Status.ACTIVE);
        cupom2.setPublished(true);

        Cupom cupom3 = new Cupom();
        cupom3.setCode("MULT02");
        cupom3.setDescription("Múltiplo 2");
        cupom3.setDiscountValue(BigDecimal.valueOf(25.0));
        cupom3.setExpirationDate(expirationDate);
        cupom3.setStatus(Status.ACTIVE);
        cupom3.setPublished(false);

        // Act
        cupomRepository.saveAll(List.of(cupom, cupom2, cupom3));
        List<Cupom> allCupoms = cupomRepository.findAll();

        // Assert
        assertEquals(3, allCupoms.size());
        assertEquals(3, cupomRepository.count());
    }
}