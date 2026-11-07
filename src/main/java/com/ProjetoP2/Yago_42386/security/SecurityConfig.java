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
				// REGRAS DE AUTORIZAÇÃO POR PERFIL (Item 7 e 9 do Edital):
				// - PERFIL RH/ADMIN: Gestão irrestrita (inclusive Folha de Pagamento)
				// - PERFIL CONSULTA: Somente leitura de dados cadastrais
				// =====================================================================

				// 1. Folha de Pagamento: Acesso estritamente restrito ao perfil RH / ADMIN
				.requestMatchers("/folhas/**", "/api/folhas-pagamento/**").hasAnyRole("RH", "ADMIN", "GESTOR_RH")

				// 2. Operações de modificação (POST, PUT, DELETE): Apenas RH / ADMIN
				.requestMatchers(HttpMethod.POST, "/colaboradores/**", "/api/**").hasAnyRole("RH", "ADMIN", "GESTOR_RH")
				.requestMatchers(HttpMethod.PUT, "/colaboradores/**", "/api/**").hasAnyRole("RH", "ADMIN", "GESTOR_RH")
				.requestMatchers(HttpMethod.DELETE, "/colaboradores/**", "/api/**").hasAnyRole("RH", "ADMIN", "GESTOR_RH")

				// 3. Gestão de Perfis e Usuários: Apenas RH / ADMIN
				.requestMatchers("/usuarios/**", "/perfis/**", "/api/usuarios/**", "/api/perfis/**").hasAnyRole("RH", "ADMIN", "GESTOR_RH")

				// 4. Qualquer outra requisição exige usuário autenticado
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
