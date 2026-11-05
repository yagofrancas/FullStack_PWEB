package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.colaborador;
import com.ProjetoP2.Yago_42386.repository.cargoRepository;
import com.ProjetoP2.Yago_42386.repository.colaboradorRepository;
import com.ProjetoP2.Yago_42386.repository.departamentoRepository;
import com.ProjetoP2.Yago_42386.repository.unidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class colaboradorService {

	private static final List<String> STATUS_VALIDOS = Arrays.asList("Ativo", "Afastado", "Férias", "Desligado");

	private final colaboradorRepository repository;
	private final departamentoRepository depRepository;
	private final cargoRepository carRepository;
	private final unidadeRepository uniRepository;

	@Autowired
	public colaboradorService(colaboradorRepository repository,
							  departamentoRepository depRepository,
							  cargoRepository carRepository,
							  unidadeRepository uniRepository) {
		this.repository = repository;
		this.depRepository = depRepository;
		this.carRepository = carRepository;
		this.uniRepository = uniRepository;
	}

	public List<colaborador> listarTodos() {
		return repository.findAll();
	}

	public Optional<colaborador> buscarPorId(Integer id) {
		return repository.findById(id);
	}

	public List<colaborador> buscarPorStatus(String status) {
		return repository.buscarPorStatus(status);
	}

	public colaborador salvar(colaborador entity) {
		String statusNormalizado = validarEFormatarStatus(entity.getStatus_colaborador());
		entity.setStatus_colaborador(statusNormalizado);

		vincularRelacionamentos(entity);
		return repository.save(entity);
	}

	public colaborador atualizar(Integer id, colaborador entity) {
		return repository.findById(id).map(existente -> {
			String statusAtual = existente.getStatus_colaborador();
			String novoStatus = validarEFormatarStatus(entity.getStatus_colaborador());

			// Validação de Transição de Status (Fase 07 / Requisitos 2.3 e 7 do Projeto)
			validarTransicaoStatus(statusAtual, novoStatus);

			existente.setCodigo_anonimizado(entity.getCodigo_anonimizado());
			existente.setEscolaridade(entity.getEscolaridade());
			existente.setRegime_contratacao(entity.getRegime_contratacao());
			existente.setModelo_trabalho(entity.getModelo_trabalho());
			existente.setSalario_base(entity.getSalario_base());
			existente.setData_nascimento(entity.getData_nascimento());
			existente.setData_admissao(entity.getData_admissao());
			existente.setStatus_colaborador(novoStatus);

			existente.setId_departamento(entity.getId_departamento());
			existente.setId_cargo(entity.getId_cargo());
			existente.setId_unidade(entity.getId_unidade());
			vincularRelacionamentos(existente);

			return repository.save(existente);
		}).orElseThrow(() -> new RuntimeException("Colaborador não encontrado com id: " + id));
	}

	public void deletar(Integer id) {
		repository.deleteById(id);
	}

	/**
	 * Validação de integridade e valores admissíveis do campo status_colaborador.
	 */
	public String validarEFormatarStatus(String status) {
		if (status == null || status.trim().isEmpty()) {
			return "Ativo";
		}
		String s = status.trim();
		for (String valido : STATUS_VALIDOS) {
			if (valido.equalsIgnoreCase(s) || (valido.equalsIgnoreCase("Férias") && s.equalsIgnoreCase("Ferias"))) {
				return valido;
			}
		}
		throw new IllegalArgumentException("Status inválido: '" + status + "'. Valores admissíveis: " + String.join(", ", STATUS_VALIDOS));
	}

	/**
	 * Máquina de Estados: Prevenção de estados inconsistentes e regras de transição válidas.
	 * Conforme Requisito 2.3 do Edital:
	 * - 'Desligado' é um estado terminal: rescisão já homologada, não pode transitar para Ativo/Férias/Afastado diretamente.
	 */
	public void validarTransicaoStatus(String statusAtual, String novoStatus) {
		if (statusAtual == null || statusAtual.equalsIgnoreCase(novoStatus)) {
			return;
		}

		if (statusAtual.equalsIgnoreCase("Desligado")) {
			throw new IllegalArgumentException("Transição inválida: Colaborador com status 'Desligado' não pode ter seu status alterado diretamente. Exige novo processo de admissão.");
		}
	}

	private void vincularRelacionamentos(colaborador entity) {
		Integer depId = entity.getId_departamento();
		if (depId != null) {
			depRepository.findById(depId).ifPresent(entity::setDepartamento);
		} else if (entity.getDepartamento() != null && entity.getDepartamento().getId_departamento() != null) {
			depRepository.findById(entity.getDepartamento().getId_departamento()).ifPresent(entity::setDepartamento);
		}

		Integer carId = entity.getId_cargo();
		if (carId != null) {
			carRepository.findById(carId).ifPresent(entity::setCargo);
		} else if (entity.getCargo() != null && entity.getCargo().getId_cargo() != null) {
			carRepository.findById(entity.getCargo().getId_cargo()).ifPresent(entity::setCargo);
		}

		Integer uniId = entity.getId_unidade();
		if (uniId != null) {
			uniRepository.findById(uniId).ifPresent(entity::setUnidade);
		} else if (entity.getUnidade() != null && entity.getUnidade().getId_unidade() != null) {
			uniRepository.findById(entity.getUnidade().getId_unidade()).ifPresent(entity::setUnidade);
		}
	}
}
