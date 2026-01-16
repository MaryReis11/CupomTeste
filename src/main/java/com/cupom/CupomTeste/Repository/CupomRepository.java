package com.cupom.CupomTeste.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cupom.CupomTeste.model.Cupom;


public interface CupomRepository  extends JpaRepository<Cupom, UUID> {
}