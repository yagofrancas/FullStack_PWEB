package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface beneficioRepository extends JpaRepository<beneficio, Integer> {
}

