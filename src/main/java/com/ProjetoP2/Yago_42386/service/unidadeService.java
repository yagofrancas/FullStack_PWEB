package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.unidade;
import com.ProjetoP2.Yago_42386.repository.unidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class unidadeService {

	private final unidadeRepository repository;

	@Autowired
	public unidadeService(unidadeRepository repository) {
		this.repository = repository;
	}

	public List<unidade> listarTodos() {
		return repository.findAll();
	}

	public Optional<unidade> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public unidade salvar(unidade entity) {
		return repository.save(entity);
	}

	public unidade atualizar(Integer id, unidade entity) {
		return repository.findById(id).map(existente -> {
			existente.setNome_unidade(entity.getNome_unidade());
			existente.setCidade(entity.getCidade());
			existente.setEstado(entity.getEstado());
			existente.setTipo_unidade(entity.getTipo_unidade());
			existente.setData_abertura(entity.getData_abertura());
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Unidade não encontrada com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

