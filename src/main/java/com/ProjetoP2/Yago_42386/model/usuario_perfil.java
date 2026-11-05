package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario_perfil")
@IdClass(usuario_perfil_id.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class usuario_perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_usuario")
	private Integer id_usuario;

	@Id
	@Column(name = "id_perfil")
	private Integer id_perfil;

	@ManyToOne
	@JoinColumn(name = "id_usuario", insertable = false, updatable = false)
	private usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_perfil", insertable = false, updatable = false)
	private perfil perfil;

	public usuario_perfil(Integer id_usuario, Integer id_perfil) {
		this.id_usuario = id_usuario;
		this.id_perfil = id_perfil;
	}

	public Integer getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(Integer id_usuario) {
		this.id_usuario = id_usuario;
	}

	public Integer getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(Integer id_perfil) {
		this.id_perfil = id_perfil;
	}

	public usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(usuario usuario) {
		this.usuario = usuario;
		if (usuario != null) {
			this.id_usuario = usuario.getId_usuario();
		}
	}

	public perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(perfil perfil) {
		this.perfil = perfil;
		if (perfil != null) {
			this.id_perfil = perfil.getId_perfil();
		}
	}
}

