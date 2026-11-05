package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.beneficio;
import com.ProjetoP2.Yago_42386.service.beneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
@CrossOrigin(origins = "*")
public class beneficioController {

	private final beneficioService service;

	@Autowired
	public beneficioController(beneficioService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<beneficio>> listarTodos() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<beneficio> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<beneficio> criar(@RequestBody beneficio entity) {
		beneficio criado = service.salvar(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<beneficio> atualizar(@PathVariable Integer id, @RequestBody beneficio entity) {
		try {
			beneficio atualizado = service.atualizar(id, entity);
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

