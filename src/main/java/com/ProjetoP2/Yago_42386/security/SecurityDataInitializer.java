package com.ProjetoP2.Yago_42386.security;

import com.ProjetoP2.Yago_42386.model.perfil;
import com.ProjetoP2.Yago_42386.model.usuario;
import com.ProjetoP2.Yago_42386.model.usuario_perfil;
import com.ProjetoP2.Yago_42386.repository.perfilRepository;
import com.ProjetoP2.Yago_42386.repository.usuarioRepository;
import com.ProjetoP2.Yago_42386.repository.usuario_perfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Inicializador de dados de segurança.
 * Garante que os perfis ('RH', 'ADMIN', 'CONSULTA') e os usuários de teste
 * estejam persistidos com hashes BCrypt válidos na inicialização.
 */
@Component
public class SecurityDataInitializer implements CommandLineRunner {

	private final usuarioRepository usuarioRepo;
	private final perfilRepository perfilRepo;
	private final usuario_perfilRepository usuarioPerfilRepo;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityDataInitializer(usuarioRepository usuarioRepo,
								   perfilRepository perfilRepo,
								   usuario_perfilRepository usuarioPerfilRepo,
								   PasswordEncoder passwordEncoder) {
		this.usuarioRepo = usuarioRepo;
		this.perfilRepo = perfilRepo;
		this.usuarioPerfilRepo = usuarioPerfilRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		// 1. Garantir existência dos Perfis fundamentais
		perfil perfilRh = obterOuCriarPerfil("RH");
		perfil perfilAdmin = obterOuCriarPerfil("ADMIN");
		perfil perfilConsulta = obterOuCriarPerfil("CONSULTA");

		// 2. Garantir usuário com perfil RH / ADMIN (Gestão completa)
		obterOuCriarUsuario("admin", "admin123", perfilAdmin);
		obterOuCriarUsuario("admin.rh", "rh123", perfilRh);
		obterOuCriarUsuario("yago.franca", "yago123", perfilRh);

		// 3. Garantir usuário com perfil CONSULTA (Apenas leitura cadastral, sem acesso à folha)
		obterOuCriarUsuario("consulta", "consulta123", perfilConsulta);
		obterOuCriarUsuario("user.consulta", "consulta123", perfilConsulta);
	}

	private perfil obterOuCriarPerfil(String nome) {
		for (perfil p : perfilRepo.findAll()) {
			if (p.getNome_perfil().equalsIgnoreCase(nome)) {
				return p;
			}
		}
		perfil novo = new perfil();
		novo.setNome_perfil(nome.toUpperCase());
		return perfilRepo.save(novo);
	}

	private void obterOuCriarUsuario(String login, String plainPassword, perfil p) {
		Optional<usuario> existente = usuarioRepo.findByLogin(login);
		usuario u;
		if (existente.isPresent()) {
			u = existente.get();
			// Se a senha não estiver no padrão BCrypt ($2a$), re-codifica
			if (u.getSenha_hash() == null || !u.getSenha_hash().startsWith("$2a$")) {
				u.setSenha_hash(passwordEncoder.encode(plainPassword));
				u = usuarioRepo.save(u);
			}
		} else {
			u = new usuario();
			u.setLogin(login);
			u.setSenha_hash(passwordEncoder.encode(plainPassword));
			u = usuarioRepo.save(u);
		}

		// Garante associação em usuario_perfil
		Integer idU = u.getId_usuario();
		Integer idP = p.getId_perfil();
		boolean vinculado = usuarioPerfilRepo.buscarPorUsuario(idU).stream()
				.anyMatch(up -> up.getId_perfil().equals(idP));

		if (!vinculado) {
			usuario_perfil up = new usuario_perfil(idU, idP);
			up.setUsuario(u);
			up.setPerfil(p);
			usuarioPerfilRepo.save(up);
		}
	}
}
