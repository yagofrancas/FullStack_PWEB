package com.ProjetoP2.Yago_42386.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class colaborador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_colaborador")
	private Integer id_colaborador;

	@Column(name = "codigo_anonimizado", length = 100)
	private String codigo_anonimizado;

	@Column(name = "escolaridade", length = 100)
	private String escolaridade;

	@Column(name = "regime_contratacao", length = 50)
	private String regime_contratacao;

	@Column(name = "modelo_trabalho", length = 50)
	private String modelo_trabalho;

	@Column(name = "salario_base")
	private Double salario_base;

	@Column(name = "data_nascimento")
	private LocalDate data_nascimento;

	@Column(name = "data_admissao")
	private LocalDate data_admissao;

	@Column(name = "status_colaborador", length = 50)
	private String status_colaborador;

	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private departamento departamento;

	@ManyToOne
	@JoinColumn(name = "id_cargo")
	private cargo cargo;

	@ManyToOne
	@JoinColumn(name = "id_unidade")
	private unidade unidade;

	@Transient
	@JsonProperty("id_departamento")
	private Integer id_departamento;

	@Transient
	@JsonProperty("id_cargo")
	private Integer id_cargo;

	@Transient
	@JsonProperty("id_unidade")
	private Integer id_unidade;

	public Integer getId_colaborador() {
		return id_colaborador;
	}

	public void setId_colaborador(Integer id_colaborador) {
		this.id_colaborador = id_colaborador;
	}

	public String getCodigo_anonimizado() {
		return codigo_anonimizado;
	}

	public void setCodigo_anonimizado(String codigo_anonimizado) {
		this.codigo_anonimizado = codigo_anonimizado;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getRegime_contratacao() {
		return regime_contratacao;
	}

	public void setRegime_contratacao(String regime_contratacao) {
		this.regime_contratacao = regime_contratacao;
	}

	public String getModelo_trabalho() {
		return modelo_trabalho;
	}

	public void setModelo_trabalho(String modelo_trabalho) {
		this.modelo_trabalho = modelo_trabalho;
	}

	public Double getSalario_base() {
		return salario_base;
	}

	public void setSalario_base(Double salario_base) {
		this.salario_base = salario_base;
	}

	public LocalDate getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(LocalDate data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public LocalDate getData_admissao() {
		return data_admissao;
	}

	public void setData_admissao(LocalDate data_admissao) {
		this.data_admissao = data_admissao;
	}

	public String getStatus_colaborador() {
		return status_colaborador;
	}

	public void setStatus_colaborador(String status_colaborador) {
		this.status_colaborador = status_colaborador;
	}

	public departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(departamento departamento) {
		this.departamento = departamento;
		if (departamento != null) {
			this.id_departamento = departamento.getId_departamento();
		}
	}

	public cargo getCargo() {
		return cargo;
	}

	public void setCargo(cargo cargo) {
		this.cargo = cargo;
		if (cargo != null) {
			this.id_cargo = cargo.getId_cargo();
		}
	}

	public unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(unidade unidade) {
		this.unidade = unidade;
		if (unidade != null) {
			this.id_unidade = unidade.getId_unidade();
		}
	}

	public Integer getId_departamento() {
		return departamento != null ? departamento.getId_departamento() : id_departamento;
	}

	public void setId_departamento(Integer id_departamento) {
		this.id_departamento = id_departamento;
	}

	public Integer getId_cargo() {
		return cargo != null ? cargo.getId_cargo() : id_cargo;
	}

	public void setId_cargo(Integer id_cargo) {
		this.id_cargo = id_cargo;
	}

	public Integer getId_unidade() {
		return unidade != null ? unidade.getId_unidade() : id_unidade;
	}

	public void setId_unidade(Integer id_unidade) {
		this.id_unidade = id_unidade;
	}
}
