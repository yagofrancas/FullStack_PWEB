package com.ProjetoP2.Yago_42386.model;

/**
 * Classe utilitária/placeholder gerada pelo Astah UML.
 * No mapeamento JPA/Java, o tipo SQL 'varchar' mapeia diretamente para java.lang.String.
 * Os atributos correspondentes nas entidades foram tipados como String com anotação @Column.
 */
public class varchar {

	private String valor;

	public varchar() {
	}

	public varchar(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return valor != null ? valor : "";
	}
}
