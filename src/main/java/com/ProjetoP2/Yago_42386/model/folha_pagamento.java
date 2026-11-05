package com.ProjetoP2.Yago_42386.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "folha_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class folha_pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_folha")
	private Integer id_folha;

	@Column(name = "data_referencia")
	private LocalDate data_referencia;

	@Column(name = "total_vencimentos")
	private Double total_vencimentos;

	@Column(name = "total_descontos")
	private Double total_descontos;

	@Column(name = "salario_liquido")
	private Double salario_liquido;

	@ManyToOne
	@JoinColumn(name = "id_colaborador")
	private colaborador colaborador;

	@Transient
	@JsonProperty("id_colaborador")
	private Integer id_colaborador;

	public Integer getId_folha() {
		return id_folha;
	}

	public void setId_folha(Integer id_folha) {
		this.id_folha = id_folha;
	}

	public LocalDate getData_referencia() {
		return data_referencia;
	}

	public void setData_referencia(LocalDate data_referencia) {
		this.data_referencia = data_referencia;
	}

	public Double getTotal_vencimentos() {
		return total_vencimentos;
	}

	public void setTotal_vencimentos(Double total_vencimentos) {
		this.total_vencimentos = total_vencimentos;
	}

	public Double getTotal_descontos() {
		return total_descontos;
	}

	public void setTotal_descontos(Double total_descontos) {
		this.total_descontos = total_descontos;
	}

	public Double getSalario_liquido() {
		return salario_liquido;
	}

	public void setSalario_liquido(Double salario_liquido) {
		this.salario_liquido = salario_liquido;
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

	public Integer getId_colaborador() {
		return colaborador != null ? colaborador.getId_colaborador() : id_colaborador;
	}

	public void setId_colaborador(Integer id_colaborador) {
		this.id_colaborador = id_colaborador;
	}
}

