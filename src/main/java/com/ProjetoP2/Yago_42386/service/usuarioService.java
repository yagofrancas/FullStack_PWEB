package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.usuario;
import com.ProjetoP2.Yago_42386.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class usuarioService {

	private final usuarioRepository repository;

	@Autowired
	public usuarioService(usuarioRepository repository) {
		this.repository = repository;
	}

	public List<usuario> listarTodos() {
		return repository.findAll();
	}

	public Optional<usuario> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public Optional<usuario> buscarPorLogin(String login) {
		return repository.findByLogin(login);
	}

	public usuario salvar(usuario entity) {
		return repository.save(entity);
	}

	public usuario atualizar(Integer id, usuario entity) {
		return repository.findById(id).map(existente -> {
			existente.setLogin(entity.getLogin());
			if (entity.getSenha_hash() != null && !entity.getSenha_hash().isBlank()) {
				existente.setSenha_hash(entity.getSenha_hash());
			}
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}
}

