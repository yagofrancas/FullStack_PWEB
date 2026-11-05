package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.colaborador;
import com.ProjetoP2.Yago_42386.service.colaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "*")
public class colaboradorController {

	private final colaboradorService service;

	@Autowired
	public colaboradorController(colaboradorService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<colaborador>> listarTodos(@RequestParam(required = false) String status) {
		if (status != null && !status.isBlank()) {
			return ResponseEntity.ok(service.buscarPorStatus(status));
		}
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<colaborador> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> criar(@RequestBody colaborador entity) {
		try {
			colaborador criado = service.salvar(entity);
			return ResponseEntity.status(HttpStatus.CREATED).body(criado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody colaborador entity) {
		try {
			colaborador atualizado = service.atualizar(id, entity);
			return ResponseEntity.ok(atualizado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
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

