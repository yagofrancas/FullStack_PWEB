package com.ProjetoP2.Yago_42386.model;

import java.io.Serializable;
import java.util.Objects;

public class usuario_perfil_id implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id_usuario;
	private Integer id_perfil;

	public usuario_perfil_id() {
	}

	public usuario_perfil_id(Integer id_usuario, Integer id_perfil) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		usuario_perfil_id that = (usuario_perfil_id) o;
		return Objects.equals(id_usuario, that.id_usuario) &&
				Objects.equals(id_perfil, that.id_perfil);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_usuario, id_perfil);
	}
}

