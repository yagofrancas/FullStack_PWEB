package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.cargo;
import com.ProjetoP2.Yago_42386.repository.cargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class cargoService {

	private final cargoRepository repository;

	@Autowired
	public cargoService(cargoRepository repository) {
		this.repository = repository;
	}

	public List<cargo> listarTodos() {
		return repository.findAll();
	}

	public Optional<cargo> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public cargo salvar(cargo entity) {
		return repository.save(entity);
	}

	public cargo atualizar(Integer id, cargo entity) {
		return repository.findById(id).map(existente -> {
			existente.setNome_cargo(entity.getNome_cargo());
			existente.setNivel(entity.getNivel());
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Cargo não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

