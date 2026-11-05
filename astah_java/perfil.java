package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class perfil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil")
	private Integer id_perfil;

	@Column(name = "nome_perfil", length = 100, nullable = false)
	private String nome_perfil;

	public Integer getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(Integer id_perfil) {
		this.id_perfil = id_perfil;
	}

	public String getNome_perfil() {
		return nome_perfil;
	}

	public void setNome_perfil(String nome_perfil) {
		this.nome_perfil = nome_perfil;
	}
}
