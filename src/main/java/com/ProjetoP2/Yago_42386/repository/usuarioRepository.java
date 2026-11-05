package com.ProjetoP2.Yago_42386.repository;

import com.ProjetoP2.Yago_42386.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface usuarioRepository extends JpaRepository<usuario, Integer> {
	Optional<usuario> findByLogin(String login);
}

