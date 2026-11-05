package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.usuario_perfil;
import com.ProjetoP2.Yago_42386.model.usuario_perfil_id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface usuario_perfilRepository extends JpaRepository<usuario_perfil, usuario_perfil_id> {

	@Query("SELECT up FROM usuario_perfil up WHERE up.id_usuario = :idUsuario")
	List<usuario_perfil> buscarPorUsuario(@Param("idUsuario") Integer idUsuario);

	@Query("SELECT up FROM usuario_perfil up WHERE up.id_perfil = :idPerfil")
	List<usuario_perfil> buscarPorPerfil(@Param("idPerfil") Integer idPerfil);
}

