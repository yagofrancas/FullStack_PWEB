package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "colaborador_beneficio")
@IdClass(colaborador_beneficio_id.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class colaborador_beneficio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_colaborador")
	private Integer id_colaborador;

	@Id
	@Column(name = "id_beneficio")
	private Integer id_beneficio;

	@ManyToOne
	@JoinColumn(name = "id_colaborador", insertable = false, updatable = false)
	private colaborador colaborador;

	@ManyToOne
	@JoinColumn(name = "id_beneficio", insertable = false, updatable = false)
	private beneficio beneficio;

	public colaborador_beneficio(Integer id_colaborador, Integer id_beneficio) {
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

	public colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(colaborador colaborador) {
		this.colaborador = colaborador;
		if (colaborador != null) {
			this.id_colaborador = colaborador.getId_colaborador();
		}
	}

	public beneficio getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(beneficio beneficio) {
		this.beneficio = beneficio;
		if (beneficio != null) {
			this.id_beneficio = beneficio.getId_beneficio();
		}
	}
}

