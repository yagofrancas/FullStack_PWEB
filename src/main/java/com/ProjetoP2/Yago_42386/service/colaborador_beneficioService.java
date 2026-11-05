package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.colaborador_beneficio;
import com.ProjetoP2.Yago_42386.model.colaborador_beneficio_id;
import com.ProjetoP2.Yago_42386.repository.beneficioRepository;
import com.ProjetoP2.Yago_42386.repository.colaboradorRepository;
import com.ProjetoP2.Yago_42386.repository.colaborador_beneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class colaborador_beneficioService {

	private final colaborador_beneficioRepository repository;
	private final colaboradorRepository colRepository;
	private final beneficioRepository benRepository;

	@Autowired
	public colaborador_beneficioService(colaborador_beneficioRepository repository,
										colaboradorRepository colRepository,
										beneficioRepository benRepository) {
		this.repository = repository;
		this.colRepository = colRepository;
		this.benRepository = benRepository;
	}

	public List<colaborador_beneficio> listarTodos() {
		return repository.findAll();
	}

	public Optional<colaborador_beneficio> buscarPorId(Integer idColaborador, Integer idBeneficio) {
		return repository.findById(new colaborador_beneficio_id(idColaborador, idBeneficio));
	}

	public List<colaborador_beneficio> buscarPorColaborador(Integer idColaborador) {
		return repository.buscarPorColaborador(idColaborador);
	}

	public List<colaborador_beneficio> buscarPorBeneficio(Integer idBeneficio) {
		return repository.buscarPorBeneficio(idBeneficio);
	}

	public colaborador_beneficio associar(Integer idColaborador, Integer idBeneficio) {
		colRepository.findById(idColaborador)
				.orElseThrow(() -> new RuntimeException("Colaborador não encontrado com id: " + idColaborador));
		benRepository.findById(idBeneficio)
				.orElseThrow(() -> new RuntimeException("Benefício não encontrado com id: " + idBeneficio));

		colaborador_beneficio cb = new colaborador_beneficio(idColaborador, idBeneficio);
		return repository.save(cb);
	}

	public void remover(Integer idColaborador, Integer idBeneficio) {
		colaborador_beneficio_id id = new colaborador_beneficio_id(idColaborador, idBeneficio);
		repository.deleteById(id);
	}
}

