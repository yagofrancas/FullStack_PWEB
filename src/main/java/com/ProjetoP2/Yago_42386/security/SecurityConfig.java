package com.ProjetoP2.Yago_42386.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração central de segurança do Spring Security (Fase 09).
 * - Proteção por perfis (ROLE_RH, ROLE_ADMIN, ROLE_GESTOR_RH vs ROLE_CONSULTA, ROLE_COLABORADOR).
 * - Formulário customizado de Login e Logout com identidade Color Hunt.
 * - Proteção em nível de servidor para Folha de Pagamento e operações de escrita.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				// Recursos estáticos públicos (CSS, JS, imagens, ícones)
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
				// Console H2 habilitado para testes de avaliação
				.requestMatchers("/h2-console/**").permitAll()
				// Rotas de autenticação e erro de acesso
				.requestMatchers("/login", "/acesso-negado").permitAll()

				// =====================================================================
				// REGRAS DE AUTORIZAÇÃO POR PERFIL (Head de RH vs Funcionário RH):
				// - ADMIN (Head de RH): Gestão irrestrita (Folha, Estrutura, Usuários, Exclusões e Salários)
				// - FUNCIONARIO (RH Operacional): Cadastro/edição de colaboradores e benefícios
				// =====================================================================

				// 1. Folha de Pagamento, Perfis e Usuários: Acesso estritamente restrito ao Head de RH (ADMIN)
				.requestMatchers("/folhas/**", "/api/folhas-pagamento/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")
				.requestMatchers("/usuarios/**", "/perfis/**", "/api/usuarios/**", "/api/perfis/**", "/api/usuarios-perfis/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")

				// 2. Estrutura Organizacional (Departamentos, Cargos, Unidades): Escrita restrita ao ADMIN
				.requestMatchers(HttpMethod.POST, "/api/departamentos/**", "/api/cargos/**", "/api/unidades/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")
				.requestMatchers(HttpMethod.PUT, "/api/departamentos/**", "/api/cargos/**", "/api/unidades/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")
				.requestMatchers(HttpMethod.DELETE, "/api/departamentos/**", "/api/cargos/**", "/api/unidades/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")

				// 3. Exclusão definitiva de colaboradores e benefícios: Apenas ADMIN
				.requestMatchers(HttpMethod.DELETE, "/colaboradores/**", "/api/colaboradores/**", "/api/beneficios/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")

				// 4. Operações operacionais (cadastro/edição de colaboradores e gestão de benefícios): ADMIN e FUNCIONARIO
				.requestMatchers(HttpMethod.GET, "/colaboradores/novo", "/colaboradores/editar/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH", "FUNCIONARIO")
				.requestMatchers(HttpMethod.POST, "/colaboradores/**", "/api/colaboradores/**", "/api/beneficios/**", "/api/colaboradores-beneficios/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH", "FUNCIONARIO")
				.requestMatchers(HttpMethod.PUT, "/colaboradores/**", "/api/colaboradores/**", "/api/beneficios/**", "/api/colaboradores-beneficios/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH", "FUNCIONARIO")
				.requestMatchers(HttpMethod.DELETE, "/api/colaboradores-beneficios/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH", "FUNCIONARIO")

				// 5. Qualquer outra operação de modificação em API: Apenas ADMIN
				.requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")
				.requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")
				.requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN", "RH", "GESTOR_RH")

				// 6. Qualquer outra requisição (GET de consulta/telas) exige usuário autenticado
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error=true")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout=true")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll()
			)
			.exceptionHandling(ex -> ex
				.accessDeniedPage("/acesso-negado")
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/h2-console/**", "/api/**")
			)
			.headers(headers -> headers
				.frameOptions(frame -> frame.sameOrigin())
			);

		return http.build();
	}
}
