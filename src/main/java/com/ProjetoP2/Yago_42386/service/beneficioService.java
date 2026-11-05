package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.beneficio;
import com.ProjetoP2.Yago_42386.repository.beneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class beneficioService {

	private final beneficioRepository repository;

	@Autowired
	public beneficioService(beneficioRepository repository) {
		this.repository = repository;
	}

	public List<beneficio> listarTodos() {
		return repository.findAll();
	}

	public Optional<beneficio> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public beneficio salvar(beneficio entity) {
		return repository.save(entity);
	}

	public beneficio atualizar(Integer id, beneficio entity) {
		return repository.findById(id).map(existente -> {
			existente.setNome_beneficio(entity.getNome_beneficio());
			existente.setTipo_beneficio(entity.getTipo_beneficio());
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Benefício não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

