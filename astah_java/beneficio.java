package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beneficio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class beneficio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_beneficio")
	private Integer id_beneficio;

	@Column(name = "nome_beneficio", length = 100, nullable = false)
	private String nome_beneficio;

	@Column(name = "tipo_beneficio", length = 50)
	private String tipo_beneficio;

	public Integer getId_beneficio() {
		return id_beneficio;
	}

	public void setId_beneficio(Integer id_beneficio) {
		this.id_beneficio = id_beneficio;
	}

	public String getNome_beneficio() {
		return nome_beneficio;
	}

	public void setNome_beneficio(String nome_beneficio) {
		this.nome_beneficio = nome_beneficio;
	}

	public String getTipo_beneficio() {
		return tipo_beneficio;
	}

	public void setTipo_beneficio(String tipo_beneficio) {
		this.tipo_beneficio = tipo_beneficio;
	}
}
