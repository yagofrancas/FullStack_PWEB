package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.folha_pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface folha_pagamentoRepository extends JpaRepository<folha_pagamento, Integer> {
	@Query("SELECT f FROM folha_pagamento f WHERE f.colaborador.id_colaborador = :idColaborador")
	List<folha_pagamento> buscarPorColaborador(@Param("idColaborador") Integer idColaborador);
}

