package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.folha_pagamento;
import com.ProjetoP2.Yago_42386.service.folha_pagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folhas-pagamento")
@CrossOrigin(origins = "*")
public class folha_pagamentoController {

	private final folha_pagamentoService service;

	@Autowired
	public folha_pagamentoController(folha_pagamentoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<folha_pagamento>> listarTodos(@RequestParam(required = false) Integer idColaborador) {
		if (idColaborador != null) {
			return ResponseEntity.ok(service.buscarPorColaborador(idColaborador));
		}
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<folha_pagamento> buscarPorId(@PathVariable Integer id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<folha_pagamento> criar(@RequestBody folha_pagamento entity) {
		folha_pagamento criado = service.salvar(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<folha_pagamento> atualizar(@PathVariable Integer id, @RequestBody folha_pagamento entity) {
		try {
			folha_pagamento atualizado = service.atualizar(id, entity);
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

