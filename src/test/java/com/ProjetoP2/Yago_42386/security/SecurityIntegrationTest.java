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

import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
				.andExpect(redirectedUrl("/login"));
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
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@DisplayName("Perfil ADMIN (Head de RH): Deve permitir acesso irrestrito a Colaboradores, Folha e Painel Admin")
	void devePermitirAcessoTotalParaPerfilAdmin() throws Exception {
		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("/colaboradores/excluir/")));

		mockMvc.perform(get("/folhas"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/colaboradores/novo"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"rh-admin-permission\"")))
				.andExpect(content().string(containsString("id=\"rh-write-permission\"")));
	}

	@Test
	@WithMockUser(username = "funcionario", roles = {"FUNCIONARIO"})
	@DisplayName("Perfil FUNCIONARIO: Pode cadastrar e editar colaboradores, mas sem Folha, Estrutura ou Exclusão")
	void deveAplicarAlcadasCorretasParaPerfilFuncionario() throws Exception {
		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("+ Novo Colaborador")))
				.andExpect(content().string(containsString("/colaboradores/editar/")))
				.andExpect(content().string(not(containsString("/colaboradores/excluir/"))));

		mockMvc.perform(get("/colaboradores/novo"))
				.andExpect(status().isOk());

		mockMvc.perform(post("/colaboradores/salvar")
						.with(csrf())
						.param("codigo_anonimizado", "COL-FUNC-TESTE")
						.param("salario_base", "4200.00")
						.param("status_colaborador", "Ativo"))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(get("/folhas"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/colaboradores/excluir/1"))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/departamentos")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"nome_departamento\":\"Teste\"}"))
				.andExpect(status().isForbidden());

		mockMvc.perform(put("/api/colaboradores/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"codigo_anonimizado\":\"COL-0001\",\"salario_base\":99999.00,\"status_colaborador\":\"Ativo\"}"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"rh-write-permission\"")))
				.andExpect(content().string(not(containsString("id=\"rh-admin-permission\""))));
	}

	@Test
	@WithMockUser(username = "user.consulta", roles = {"CONSULTA"})
	@DisplayName("Perfil CONSULTA: Painel inicial deve ocultar flag de escrita e /colaboradores deve exibir botão Detalhes")
	void deveExibirInterfaceSomenteLeituraEDetalhesParaPerfilConsulta() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(not(containsString("id=\"rh-write-permission\""))));

		mockMvc.perform(get("/colaboradores"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Detalhes")))
				.andExpect(content().string(not(containsString("/colaboradores/editar/"))));
	}

	@Test
	@WithMockUser(username = "user.consulta", roles = {"CONSULTA"})
	@DisplayName("Perfil CONSULTA: Deve permitir acesso à tela de detalhes de colaborador existente")
	void devePermitirAcessoAosDetalhesDoColaboradorParaPerfilConsulta() throws Exception {
		mockMvc.perform(get("/colaboradores/detalhes/1"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Detalhes do Colaborador")));
	}

	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@DisplayName("Perfil ADMIN: Painel inicial deve habilitar flag administrativa de escrita")
	void deveHabilitarAcoesEscritaNoPainelParaPerfilAdmin() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("id=\"rh-admin-permission\"")))
				.andExpect(content().string(containsString("id=\"rh-write-permission\"")));
	}
}
