package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.folha_pagamento;
import com.ProjetoP2.Yago_42386.repository.colaboradorRepository;
import com.ProjetoP2.Yago_42386.repository.folha_pagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class folha_pagamentoService {

	private final folha_pagamentoRepository repository;
	private final colaboradorRepository colRepository;

	@Autowired
	public folha_pagamentoService(folha_pagamentoRepository repository, colaboradorRepository colRepository) {
		this.repository = repository;
		this.colRepository = colRepository;
	}

	public List<folha_pagamento> listarTodos() {
		return repository.findAll();
	}

	public Optional<folha_pagamento> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public List<folha_pagamento> buscarPorColaborador(Integer idColaborador) {
		return repository.buscarPorColaborador(idColaborador);
	}

	public folha_pagamento salvar(folha_pagamento entity) {
		vincularColaborador(entity);
		calcularSalarioLiquido(entity);
		return repository.save(entity);
	}

	public folha_pagamento atualizar(Integer id, folha_pagamento entity) {
		return repository.findById(id).map(existente -> {
			existente.setData_referencia(entity.getData_referencia());
			existente.setTotal_vencimentos(entity.getTotal_vencimentos());
			existente.setTotal_descontos(entity.getTotal_descontos());
			existente.setSalario_liquido(entity.getSalario_liquido());
			existente.setId_colaborador(entity.getId_colaborador());

			vincularColaborador(existente);
			calcularSalarioLiquido(existente);
			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Folha de pagamento não encontrada com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}

	private void vincularColaborador(folha_pagamento entity) {
		Integer colId = entity.getId_colaborador();
		if (colId != null) {
			colRepository.findById(colId).ifPresent(entity::setColaborador);
		} else if (entity.getColaborador() != null && entity.getColaborador().getId_colaborador() != null) {
			colRepository.findById(entity.getColaborador().getId_colaborador()).ifPresent(entity::setColaborador);
		}
	}

	private void calcularSalarioLiquido(folha_pagamento entity) {
		if (entity.getSalario_liquido() == null || entity.getSalario_liquido() == 0.0) {
			double venc = entity.getTotal_vencimentos() != null ? entity.getTotal_vencimentos() : 0.0;
			double desc = entity.getTotal_descontos() != null ? entity.getTotal_descontos() : 0.0;
			entity.setSalario_liquido(venc - desc);
		}
	}
}

