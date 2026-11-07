package com.ProjetoP2.Yago_42386.security;

import com.ProjetoP2.Yago_42386.model.perfil;
import com.ProjetoP2.Yago_42386.model.usuario;
import com.ProjetoP2.Yago_42386.model.usuario_perfil;
import com.ProjetoP2.Yago_42386.repository.perfilRepository;
import com.ProjetoP2.Yago_42386.repository.usuarioRepository;
import com.ProjetoP2.Yago_42386.repository.usuario_perfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de autenticação integrado ao banco de dados relacional H2.
 * Carrega credenciais da entidade 'usuario' e os papéis de 'perfil' através de 'usuario_perfil'.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final usuarioRepository usuarioRepo;
	private final usuario_perfilRepository usuarioPerfilRepo;
	private final perfilRepository perfilRepo;

	@Autowired
	public CustomUserDetailsService(usuarioRepository usuarioRepo,
								   usuario_perfilRepository usuarioPerfilRepo,
								   perfilRepository perfilRepo) {
		this.usuarioRepo = usuarioRepo;
		this.usuarioPerfilRepo = usuarioPerfilRepo;
		this.perfilRepo = perfilRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		usuario user = usuarioRepo.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

		List<usuario_perfil> vinculos = usuarioPerfilRepo.buscarPorUsuario(user.getId_usuario());
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (usuario_perfil up : vinculos) {
			perfil p = up.getPerfil();
			if (p == null && up.getId_perfil() != null) {
				p = perfilRepo.findById(up.getId_perfil()).orElse(null);
			}
			if (p != null && p.getNome_perfil() != null) {
				String nome = p.getNome_perfil().trim().toUpperCase();
				String role = nome.startsWith("ROLE_") ? nome : "ROLE_" + nome;
				authorities.add(new SimpleGrantedAuthority(role));
			}
		}

		// Se o usuário não tiver perfis explicitamente associados, atribui ROLE_CONSULTA por padrão
		if (authorities.isEmpty()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_CONSULTA"));
		}

		return new User(user.getLogin(), user.getSenha_hash(), authorities);
	}
}
