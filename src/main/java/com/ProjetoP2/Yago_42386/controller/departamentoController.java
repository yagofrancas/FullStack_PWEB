package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.departamento;
import com.ProjetoP2.Yago_42386.service.departamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin(origins = "*")
public class departamentoController {

	private final departamentoService service;

	@Autowired
	public departamentoController(departamentoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<departamento>> listarTodos() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<departamento> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<departamento> criar(@RequestBody departamento entity) {
		departamento criado = service.salvar(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<departamento> atualizar(@PathVariable Integer id, @RequestBody departamento entity) {
		try {
			departamento atualizado = service.atualizar(id, entity);
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

