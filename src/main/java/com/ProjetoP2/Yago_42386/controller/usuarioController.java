package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.usuario;
import com.ProjetoP2.Yago_42386.service.usuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class usuarioController {

	private final usuarioService service;

	@Autowired
	public usuarioController(usuarioService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<usuario>> listarTodos() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<usuario> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<usuario> criar(@RequestBody usuario entity) {
		usuario criado = service.salvar(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<usuario> atualizar(@PathVariable Integer id, @RequestBody usuario entity) {
		try {
			usuario atualizado = service.atualizar(id, entity);
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

