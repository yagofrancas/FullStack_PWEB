package com.ProjetoP2.Yago_42386.model;

import java.time.LocalDate;

/**
 * Classe utilitária/placeholder gerada pelo Astah UML.
 * No mapeamento JPA/Java, o tipo SQL 'date' mapeia para java.time.LocalDate (ou java.sql.Date).
 * Os atributos correspondentes nas entidades foram tipados como java.time.LocalDate com anotação @Column.
 */
public class date {

	private LocalDate valor;

	public date() {
	}

	public date(LocalDate valor) {
		this.valor = valor;
	}

	public LocalDate getValor() {
		return valor;
	}

	public void setValor(LocalDate valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return valor != null ? valor.toString() : "";
	}
}
