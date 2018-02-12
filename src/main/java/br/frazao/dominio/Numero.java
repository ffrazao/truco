package br.frazao.dominio;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Numero {

	AS("Ás", 1), CINCO("Cinco", 5), DAMA("Dama", 12), DEZ("Dez", 10), DOIS("Dois", 2), NOVE("Nove", 9), OITO("Oito", 8), QUATRO("Quatro", 4), REI("Rei", 13), SEIS("Seis", 6), SETE("Sete", 7), TRES("Três", 3), VALETE("Valete", 11);

	public static Numero[] valoresOrdenados() {
		return Stream.of(values()).sorted((n1, n2) -> Integer.compare(n1.getOrdem(), n2.getOrdem())).collect(Collectors.toList()).toArray(new Numero[Numero.values().length]);
	}

	private String descricao;

	private int ordem;

	private Numero(String descricao, int ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return getDescricao();
	}

}