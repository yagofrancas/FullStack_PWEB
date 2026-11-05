package com.ProjetoP2.Yago_42386.model;

import java.io.Serializable;
import java.util.Objects;

public class colaborador_beneficio_id implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id_colaborador;
	private Integer id_beneficio;

	public colaborador_beneficio_id() {
	}

	public colaborador_beneficio_id(Integer id_colaborador, Integer id_beneficio) {
		this.id_colaborador = id_colaborador;
		this.id_beneficio = id_beneficio;
	}

	public Integer getId_colaborador() {
		return id_colaborador;
	}

	public void setId_colaborador(Integer id_colaborador) {
		this.id_colaborador = id_colaborador;
	}

	public Integer getId_beneficio() {
		return id_beneficio;
	}

	public void setId_beneficio(Integer id_beneficio) {
		this.id_beneficio = id_beneficio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		colaborador_beneficio_id that = (colaborador_beneficio_id) o;
		return Objects.equals(id_colaborador, that.id_colaborador) &&
				Objects.equals(id_beneficio, that.id_beneficio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_colaborador, id_beneficio);
	}
}

