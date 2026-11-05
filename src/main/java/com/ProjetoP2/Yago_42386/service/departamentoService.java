package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.departamento;
import com.ProjetoP2.Yago_42386.repository.departamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class departamentoService {

	private final departamentoRepository repository;

	@Autowired
	public departamentoService(departamentoRepository repository) {
		this.repository = repository;
	}

	public List<departamento> listarTodos() {
		return repository.findAll();
	}

	public Optional<departamento> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public departamento salvar(departamento entity) {
		return repository.save(entity);
	}

	public departamento atualizar(Integer id, departamento entity) {
		return repository.findById(id).map(existente -> {
			existente.setNome_departamento(entity.getNome_departamento());
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Departamento não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

