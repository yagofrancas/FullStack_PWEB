package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.usuario_perfil;
import com.ProjetoP2.Yago_42386.model.usuario_perfil_id;
import com.ProjetoP2.Yago_42386.repository.perfilRepository;
import com.ProjetoP2.Yago_42386.repository.usuarioRepository;
import com.ProjetoP2.Yago_42386.repository.usuario_perfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class usuario_perfilService {

	private final usuario_perfilRepository repository;
	private final usuarioRepository usuRepository;
	private final perfilRepository perRepository;

	@Autowired
	public usuario_perfilService(usuario_perfilRepository repository,
								 usuarioRepository usuRepository,
								 perfilRepository perRepository) {
		this.repository = repository;
		this.usuRepository = usuRepository;
		this.perRepository = perRepository;
	}

	public List<usuario_perfil> listarTodos() {
		return repository.findAll();
	}

	public Optional<usuario_perfil> buscarPorId(Integer idUsuario, Integer idPerfil) {
		return repository.findById(new usuario_perfil_id(idUsuario, idPerfil));
	}

	public List<usuario_perfil> buscarPorUsuario(Integer idUsuario) {
		return repository.buscarPorUsuario(idUsuario);
	}

	public List<usuario_perfil> buscarPorPerfil(Integer idPerfil) {
		return repository.buscarPorPerfil(idPerfil);
	}

	public usuario_perfil associar(Integer idUsuario, Integer idPerfil) {
		usuRepository.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + idUsuario));
		perRepository.findById(idPerfil)
				.orElseThrow(() -> new RuntimeException("Perfil não encontrado com id: " + idPerfil));

		usuario_perfil up = new usuario_perfil(idUsuario, idPerfil);
		return repository.save(up);
	}

	public void remover(Integer idUsuario, Integer idPerfil) {
		usuario_perfil_id id = new usuario_perfil_id(idUsuario, idPerfil);
		repository.deleteById(id);
	}
}

