package com.ProjetoP2.Yago_42386.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "unidade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class unidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_unidade")
	private Integer id_unidade;

	@Column(name = "nome_unidade", length = 100, nullable = false)
	private String nome_unidade;

	@Column(name = "cidade", length = 100)
	private String cidade;

	@Column(name = "estado", length = 50)
	private String estado;

	@Column(name = "tipo_unidade", length = 50)
	private String tipo_unidade;

	@Column(name = "data_abertura")
	private LocalDate data_abertura;

	public Integer getId_unidade() {
		return id_unidade;
	}

	public void setId_unidade(Integer id_unidade) {
		this.id_unidade = id_unidade;
	}

	public String getNome_unidade() {
		return nome_unidade;
	}

	public void setNome_unidade(String nome_unidade) {
		this.nome_unidade = nome_unidade;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipo_unidade() {
		return tipo_unidade;
	}

	public void setTipo_unidade(String tipo_unidade) {
		this.tipo_unidade = tipo_unidade;
	}

	public LocalDate getData_abertura() {
		return data_abertura;
	}

	public void setData_abertura(LocalDate data_abertura) {
		this.data_abertura = data_abertura;
	}
}

