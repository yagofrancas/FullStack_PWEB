package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.repository.cargoRepository;
import com.ProjetoP2.Yago_42386.repository.colaboradorRepository;
import com.ProjetoP2.Yago_42386.repository.departamentoRepository;
import com.ProjetoP2.Yago_42386.repository.unidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - Regras de Negócio e Transição de Status do Colaborador")
class ColaboradorServiceTest {

	private colaboradorService service;
	private colaboradorRepository repository;
	private departamentoRepository depRepository;
	private cargoRepository carRepository;
	private unidadeRepository uniRepository;

	@BeforeEach
	void setUp() {
		repository = Mockito.mock(colaboradorRepository.class);
		depRepository = Mockito.mock(departamentoRepository.class);
		carRepository = Mockito.mock(cargoRepository.class);
		uniRepository = Mockito.mock(unidadeRepository.class);
		service = new colaboradorService(repository, depRepository, carRepository, uniRepository);
	}

	@Test
	@DisplayName("Deve validar e formatar status válidos com sucesso")
	void deveValidarStatusValidos() {
		assertEquals("Ativo", service.validarEFormatarStatus("ativo"));
		assertEquals("Afastado", service.validarEFormatarStatus("AFASTADO"));
		assertEquals("Férias", service.validarEFormatarStatus("ferias"));
		assertEquals("Férias", service.validarEFormatarStatus("Férias"));
		assertEquals("Desligado", service.validarEFormatarStatus("desligado"));
	}

	@Test
	@DisplayName("Deve rejeitar status nulo ou vazio aplicando 'Ativo' por padrão")
	void deveAplicarAtivoQuandoStatusVazio() {
		assertEquals("Ativo", service.validarEFormatarStatus(null));
		assertEquals("Ativo", service.validarEFormatarStatus("  "));
	}

	@Test
	@DisplayName("Deve lançar exceção para status com valor arbitrário ou inválido")
	void deveRejeitarStatusInvalido() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			service.validarEFormatarStatus("Suspenso");
		});
		assertTrue(ex.getMessage().contains("Status inválido"));
	}

	@Test
	@DisplayName("Deve permitir transições normais de status (Ativo -> Férias, Ativo -> Afastado, Ativo -> Desligado)")
	void devePermitirTransicoesValidas() {
		assertDoesNotThrow(() -> service.validarTransicaoStatus("Ativo", "Férias"));
		assertDoesNotThrow(() -> service.validarTransicaoStatus("Ativo", "Afastado"));
		assertDoesNotThrow(() -> service.validarTransicaoStatus("Ativo", "Desligado"));
		assertDoesNotThrow(() -> service.validarTransicaoStatus("Férias", "Ativo"));
		assertDoesNotThrow(() -> service.validarTransicaoStatus("Afastado", "Ativo"));
	}

	@Test
	@DisplayName("Máquina de Estados: Deve bloquear transição a partir de colaborador 'Desligado' (Estado Terminal)")
	void deveBloquearTransicaoAPartirDeDesligado() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			service.validarTransicaoStatus("Desligado", "Ativo");
		});
		assertTrue(ex.getMessage().contains("não pode ter seu status alterado diretamente"));

		assertThrows(IllegalArgumentException.class, () -> {
			service.validarTransicaoStatus("Desligado", "Férias");
		});
	}
}
