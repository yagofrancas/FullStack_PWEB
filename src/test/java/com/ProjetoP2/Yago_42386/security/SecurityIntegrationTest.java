package com.ProjetoP2.Yago_42386.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@DisplayName("Testes Automatizados de Segurança e Permissões de Perfil (Fase 09 / Item 7 do Edital)")
class SecurityIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	@DisplayName("Deve permitir acesso público à tela de login sem autenticação")
	void devePermitirAcessoPublicoAoLogin() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve redirecionar para /login ao tentar acessar recurso protegido anonimamente")
	void deveRedirecionarParaLoginQuandoNaoAutenticado() throws Exception {
		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "user.consulta", roles = {"CONSULTA"})
	@DisplayName("Perfil CONSULTA: Deve permitir visualização de colaboradores")
	void devePermitirConsultaDeColaboradoresParaPerfilConsulta() throws Exception {
		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "user.consulta", roles = {"CONSULTA"})
	@DisplayName("Perfil CONSULTA: Deve bloquear acesso à Folha de Pagamento no servidor (HTTP 403 Forbidden)")
	void deveBloquearAcessoAFolhaParaPerfilConsulta() throws Exception {
		mockMvc.perform(get("/folhas"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "user.consulta", roles = {"CONSULTA"})
	@DisplayName("Perfil CONSULTA: Deve bloquear operações de escrita (POST) no servidor (HTTP 403 Forbidden)")
	void deveBloquearOperacaoEscritaParaPerfilConsulta() throws Exception {
		mockMvc.perform(post("/colaboradores/salvar")
						.with(csrf())
						.param("codigo_anonimizado", "COL-TESTE")
						.param("salario_base", "3000.00")
						.param("status_colaborador", "Ativo"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "admin.rh", roles = {"RH"})
	@DisplayName("Perfil RH: Deve permitir acesso irrestrito a Colaboradores e Folha de Pagamento")
	void devePermitirAcessoTotalParaPerfilRH() throws Exception {
		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/folhas"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/colaboradores/novo"))
				.andExpect(status().isOk());
	}
}
