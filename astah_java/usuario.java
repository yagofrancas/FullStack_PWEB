package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer id_usuario;

	@Column(name = "login", length = 100, nullable = false, unique = true)
	private String login;

	@Column(name = "senha_hash", length = 255, nullable = false)
	private String senha_hash;

	public Integer getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(Integer id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha_hash() {
		return senha_hash;
	}

	public void setSenha_hash(String senha_hash) {
		this.senha_hash = senha_hash;
	}
}
