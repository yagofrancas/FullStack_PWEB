package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class departamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_departamento")
	private Integer id_departamento;

	@Column(name = "nome_departamento", length = 100, nullable = false)
	private String nome_departamento;

	public Integer getId_departamento() {
		return id_departamento;
	}

	public void setId_departamento(Integer id_departamento) {
		this.id_departamento = id_departamento;
	}

	public String getNome_departamento() {
		return nome_departamento;
	}

	public void setNome_departamento(String nome_departamento) {
		this.nome_departamento = nome_departamento;
	}
}

