package com.ProjetoP2.Yago_42386.controller;

import com.ProjetoP2.Yago_42386.model.colaborador_beneficio;
import com.ProjetoP2.Yago_42386.service.colaborador_beneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores-beneficios")
@CrossOrigin(origins = "*")
public class colaborador_beneficioController {

	private final colaborador_beneficioService service;

	@Autowired
	public colaborador_beneficioController(colaborador_beneficioService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<colaborador_beneficio>> listarTodos(
			@RequestParam(required = false) Integer idColaborador,
			@RequestParam(required = false) Integer idBeneficio) {
		if (idColaborador != null) {
			return ResponseEntity.ok(service.buscarPorColaborador(idColaborador));
		}
		if (idBeneficio != null) {
			return ResponseEntity.ok(service.buscarPorBeneficio(idBeneficio));
		}
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/{idColaborador}/{idBeneficio}")
	public ResponseEntity<colaborador_beneficio> buscarPorId(
			@PathVariable Integer idColaborador,
			@PathVariable Integer idBeneficio) {
		return service.buscarPorId(idColaborador, idBeneficio)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> criar(@RequestBody colaborador_beneficio entity) {
		try {
			colaborador_beneficio criado = service.associar(entity.getId_colaborador(), entity.getId_beneficio());
			return ResponseEntity.status(HttpStatus.CREATED).body(criado);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{idColaborador}/{idBeneficio}")
	public ResponseEntity<Void> deletar(
			@PathVariable Integer idColaborador,
			@PathVariable Integer idBeneficio) {
		try {
			service.remover(idColaborador, idBeneficio);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}

