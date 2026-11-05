package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface departamentoRepository extends JpaRepository<departamento, Integer> {
}

