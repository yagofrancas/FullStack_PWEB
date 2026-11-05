package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface colaboradorRepository extends JpaRepository<colaborador, Integer> {
	@Query("SELECT c FROM colaborador c WHERE c.status_colaborador = :status")
	List<colaborador> buscarPorStatus(@Param("status") String status);
}
