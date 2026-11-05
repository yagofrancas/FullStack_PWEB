package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.usuario_perfil;
import com.ProjetoP2.Yago_42386.service.usuario_perfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios-perfis")
@CrossOrigin(origins = "*")
public class usuario_perfilController {

	private final usuario_perfilService service;

	@Autowired
	public usuario_perfilController(usuario_perfilService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<usuario_perfil>> listarTodos(
			@RequestParam(required = false) Integer idUsuario,
			@RequestParam(required = false) Integer idPerfil) {
		if (idUsuario != null) {
			return ResponseEntity.ok(service.buscarPorUsuario(idUsuario));
		}
		if (idPerfil != null) {
			return ResponseEntity.ok(service.buscarPorPerfil(idPerfil));
		}
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{idUsuario}/{idPerfil}")
	public ResponseEntity<usuario_perfil> buscarPorId(
			@PathVariable Integer idUsuario,
			@PathVariable Integer idPerfil) {
		return service.buscarPorId(idUsuario, idPerfil)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> criar(@RequestBody usuario_perfil entity) {
		try {
			usuario_perfil criado = service.associar(entity.getId_usuario(), entity.getId_perfil());
			return ResponseEntity.status(HttpStatus.CREATED).body(criado);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{idUsuario}/{idPerfil}")
	public ResponseEntity<Void> deletar(
			@PathVariable Integer idUsuario,
			@PathVariable Integer idPerfil) {
		try {
			service.remover(idUsuario, idPerfil);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}

