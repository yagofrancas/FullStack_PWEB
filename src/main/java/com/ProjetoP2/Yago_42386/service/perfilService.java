package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.perfil;
import com.ProjetoP2.Yago_42386.repository.perfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class perfilService {

	private final perfilRepository repository;

	@Autowired
	public perfilService(perfilRepository repository) {
		this.repository = repository;
	}

	public List<perfil> listarTodos() {
		return repository.findAll();
	}

	public Optional<perfil> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public perfil salvar(perfil entity) {
		return repository.save(entity);
	}

	public perfil atualizar(Integer id, perfil entity) {
		return repository.findById(id).map(existente -> {
			existente.setNome_perfil(entity.getNome_perfil());
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Perfil não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

