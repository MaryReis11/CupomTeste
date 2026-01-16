package com.cupom.CupomTeste.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cupom.CupomTeste.model.Cupom;

@Repository
public interface CupomRepository  extends JpaRepository<Cupom, UUID> {
	Optional<Cupom> findById(UUID id);

}