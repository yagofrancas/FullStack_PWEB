package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.perfil;
import com.ProjetoP2.Yago_42386.service.perfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = "*")
public class perfilController {

	private final perfilService service;

	@Autowired
	public perfilController(perfilService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<perfil>> listarTodos() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<perfil> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<perfil> criar(@RequestBody perfil entity) {
		perfil criado = service.salvar(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<perfil> atualizar(@PathVariable Integer id, @RequestBody perfil entity) {
		try {
			perfil atualizado = service.atualizar(id, entity);
			return ResponseEntity.ok(atualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Integer id) {
		try {
			service.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}

