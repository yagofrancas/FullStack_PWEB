package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.colaborador_beneficio;
import com.ProjetoP2.Yago_42386.model.colaborador_beneficio_id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface colaborador_beneficioRepository extends JpaRepository<colaborador_beneficio, colaborador_beneficio_id> {

	@Query("SELECT cb FROM colaborador_beneficio cb WHERE cb.id_colaborador = :idColaborador")
	List<colaborador_beneficio> buscarPorColaborador(@Param("idColaborador") Integer idColaborador);

	@Query("SELECT cb FROM colaborador_beneficio cb WHERE cb.id_beneficio = :idBeneficio")
	List<colaborador_beneficio> buscarPorBeneficio(@Param("idBeneficio") Integer idBeneficio);
}

