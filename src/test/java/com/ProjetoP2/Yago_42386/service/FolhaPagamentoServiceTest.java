package com.ProjetoP2.Yago_42386.service;

import com.ProjetoP2.Yago_42386.model.folha_pagamento;
import com.ProjetoP2.Yago_42386.repository.colaboradorRepository;
import com.ProjetoP2.Yago_42386.repository.folha_pagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - Cálculo de Folha de Pagamento")
class FolhaPagamentoServiceTest {

	private folha_pagamentoService service;
	private folha_pagamentoRepository repository;
	private colaboradorRepository colRepository;

	@BeforeEach
	void setUp() {
		repository = Mockito.mock(folha_pagamentoRepository.class);
		colRepository = Mockito.mock(colaboradorRepository.class);
		service = new folha_pagamentoService(repository, colRepository);
	}

	@Test
	@DisplayName("Deve calcular corretamente o salário líquido (Vencimentos - Descontos)")
	void deveCalcularSalarioLiquido() {
		folha_pagamento folha = new folha_pagamento();
		folha.setTotal_vencimentos(10000.0);
		folha.setTotal_descontos(2500.0);
		folha.setSalario_liquido(null);

		service.calcularSalarioLiquido(folha);

		assertEquals(7500.0, folha.getSalario_liquido(), 0.001);
	}

	@Test
	@DisplayName("Deve corrigir salário líquido divergente ou manipulado incorretamente")
	void deveSobrescreverSalarioLiquidoIncorreto() {
		folha_pagamento folha = new folha_pagamento();
		folha.setTotal_vencimentos(5000.0);
		folha.setTotal_descontos(1000.0);
		folha.setSalario_liquido(9999.0); // Valor errado

		service.calcularSalarioLiquido(folha);

		assertEquals(4000.0, folha.getSalario_liquido(), 0.001);
	}

	@Test
	@DisplayName("Deve tratar valores nulos considerando zero nos cálculos")
	void deveTratarValoresNulosComoZero() {
		folha_pagamento folha = new folha_pagamento();
		folha.setTotal_vencimentos(null);
		folha.setTotal_descontos(null);

		service.calcularSalarioLiquido(folha);

		assertEquals(0.0, folha.getSalario_liquido(), 0.001);
	}
}

